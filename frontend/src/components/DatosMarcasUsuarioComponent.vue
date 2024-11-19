<template>
  <v-container>
    <v-card class="flex">
      <ListaCrudComponent
        :items="marcasArray"
        :cargando="false"
        :permiso-creacion="false"
        @editar="editarMarca"
        @eliminar="borrarMarca"
        titulo="Marcas Personales"
      >
        <template v-slot:titulo="{ item }">
          <div
            v-if="
              this.tipoCargaEjercicio(item.nombre) == 'VAM' ||
              this.tipoCargaEjercicio(item.nombre) == 'TIEMPO' ||
              item.nombre == 'VAM'
            "
          >
            {{ item.nombre }} : {{ formatoTiempo(item.cantidad) }} min/km
          </div>
          <div v-else>{{ item.nombre }} : {{ item.cantidad }}</div>
        </template>
      </ListaCrudComponent>
      <div class="botones">
        <FabBotonComponent
          class="boton claro"
          @click="agregarMarcaPersonal"
        ></FabBotonComponent>
        <FabBotonComponent
          icon="mdi-calculator"
          class="calculadora"
          @click="calculadora = true"
        ></FabBotonComponent>
      </div>
    </v-card>

    <v-dialog v-model="agregarMarca" max-width="400">
      <v-card>
        <v-card-title>{{
          editar ? "Editar Marca" : "Agregar Marca"
        }}</v-card-title>
        <v-card-text v-if="!editar">
          <b>Ejercicio</b>
          <v-autocomplete
            v-model="ejercicio"
            label="Ejercicio"
            :items="ejerciciosFiltrados"
            item-title="nombre"
            item-value="nombre"
            :return-object="false"
            required
            class="placeholder"
            placeholder="Extensiones"
            clearable
            solo
          ></v-autocomplete>
          <b>Tipo de carga</b>:
          <p>{{ tipo }}</p>
          <b v-if="ejercicio">Cantidad</b>
          <v-text-field
            v-model="cantidad"
            v-if="tipo == 'RM' || tipo == 'Repeticiones'"
            type="number"
            min="0"
          ></v-text-field>
          <InputTiempoComponent
            v-if="tipo == 'VAM' || tipo == 'Tiempo'"
            @nuevo-valor="actualizarCantidad"
            :valor-inicial="cantidad"
          ></InputTiempoComponent>
        </v-card-text>
        <v-card-text v-else>
          <b>Ejercicio</b>
          <v-text-field disabled="true" v-model="ejercicio"></v-text-field>
          <b>Tipo de carga</b>:
          <p>{{ tipo }}</p>
          <b v-if="ejercicio">Cantidad</b>
          <v-text-field
            v-model="cantidad"
            v-if="tipo == 'RM' || tipo == 'Repeticiones'"
            type="number"
            min="0"
          ></v-text-field>
          <InputTiempoComponent
            v-if="tipo == 'VAM' || tipo == 'Tiempo'"
            @nuevo-valor="actualizarCantidad"
            :valor-inicial="cantidad"
          ></InputTiempoComponent>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="cerrarAgregarMarca" class="rechazo"
            >Cancelar</v-btn
          >
          <v-btn text @click="confirmarAgregarMarca" class="claro"
            >Confirmar</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="calculadora">
      <CalculadoraMarcasComponent
        @guardar-VAM="guardarVAM"
        @guardar-RM="guardarRM"
        @cerrar="calculadora = false"
      >
      </CalculadoraMarcasComponent>
    </v-dialog>
  </v-container>
</template>

<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";
import InputTiempoComponent from "@/components/comun/InputTiempoComponent.vue";
import CalculadoraMarcasComponent from "@/components/CalculadoraMarcasComponent.vue";
import { useAlertasStore } from "@/store/alertasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useEjerciciosStore } from "@/store/ejerciciosStore";
import { mapActions, mapState } from "pinia";

export default {
  components: {
    ListaCrudComponent,
    FabBotonComponent,
    InputTiempoComponent,
    CalculadoraMarcasComponent,
  },
  props: {
    marcasArray: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      ejercicio: null,
      cantidad: 0,
      agregarMarca: false,
      ejerciciosFiltrados: [],
      editar: false,
      calculadora: false,
    };
  },
  computed: {
    ...mapState(useUsuariosStore, ["marcas", "username", "perfil"]),
    ...mapState(useEjerciciosStore, ["ejerciciosRegistrados"]),
    tipo() {
      let tipo = "";
      if (this.ejercicio) {
        if (this.tipoCargaEjercicio(this.ejercicio) == "REPS") {
          tipo = "Repeticiones";
        } else if (this.tipoCargaEjercicio(this.ejercicio) == "RM") {
          tipo = "1 RM";
        } else if (
          this.tipoCargaEjercicio(this.ejercicio) == "VAM" ||
          this.ejercicio == "VAM"
        ) {
          tipo = "VAM";
        } else if (this.tipoCargaEjercicio(this.ejercicio) == "TIEMPO") {
          tipo = "Tiempo";
        }
      }
      return tipo;
    },
  },
  methods: {
    ...mapActions(useUsuariosStore, ["actualizarMarcas", "eliminarMarca"]),
    ...mapActions(useEjerciciosStore, [
      "cargarEjercicios",
      "tipoCargaEjercicio",
    ]),
    ...mapActions(useAlertasStore, ["mostrarAlerta"]),
    formatoTiempo(cantidad) {
      let minutos = Math.floor(cantidad / 60)
        .toString()
        .padStart(2, "0");
      let segundos = (cantidad % 60).toString().padStart(2, "0");
      return `${minutos}:${segundos}`;
    },
    async agregarMarcaPersonal() {
      try {
        await this.cargarEjercicios();
        this.filtrarEjercicios();
        this.agregarMarca = true;
      } catch (error) {
        this.mostrarAlerta(
          "No se ha podido agregar la marca personal",
          "error"
        );
      }
    },
    cerrarAgregarMarca() {
      this.agregarMarca = false;
      this.editar = false;
    },
    async confirmarAgregarMarca() {
      let marca = {
        [this.ejercicio]: this.cantidad,
      };
      await this.actualizarMarcas(marca);
      this.$emit("actualizarMarcasArray");
      this.mostrarAlerta("Marca agregada", "success");
      this.ejercicio = null;
      this.cerrarAgregarMarca();
    },
    actualizarCantidad(tiempoTotal) {
      this.cantidad = tiempoTotal;
    },
    filtrarEjercicios() {
      this.ejerciciosFiltrados = this.ejerciciosRegistrados.filter(
        (ejercicio) => {
          return !this.marcas.hasOwnProperty(ejercicio.nombre);
        }
      );
      this.ejerciciosFiltrados = this.ejerciciosFiltrados.filter(
        (ejercicio) => {
          return ejercicio.tipoCarga != "VAM";
        }
      );
      if (!this.marcas.hasOwnProperty("VAM")) {
        this.ejerciciosFiltrados.push({ nombre: "VAM" });
      }
    },
    async editarMarca(marca) {
      this.editar = true;
      this.agregarMarca = true;
      this.ejercicio = marca.nombre;
      this.cantidad = marca.cantidad;
    },
    async borrarMarca(marca) {
      this.ejercicio = marca.nombre;
      let marcasUsuario = this.marcas;
      delete marcasUsuario[this.ejercicio];
      let usuario = {
        nombre: this.username,
        rol: this.perfil,
        marcas: marcasUsuario,
      };
      await this.eliminarMarca(usuario);
      this.$emit("actualizarMarcasArray");
    },
    async guardarVAM(cantidad) {
      this.ejercicio = "VAM";
      this.cantidad = cantidad;
      await this.confirmarAgregarMarca();
      this.calculadora = false;
    },
    async guardarRM(marca) {
      this.ejercicio = marca.nombre;
      this.cantidad = marca.cantidad;
      await this.confirmarAgregarMarca();
      this.calculadora = false;
    },
  },
  created() {
    this.cargarEjercicios();
  },
};
</script>

<style scoped>
.flex {
  display: flex;
  flex-flow: column;
}
.v-card {
  max-width: 500px;
  margin: auto;
}
.botones {
  position: absolute; 
  bottom: -10px;
  right: -10px;
  display: flex;
  gap: 10px;
}

.boton, .calculadora {
  position: relative !important;
  margin: 0;
  width: fit-content;
  border-radius: 50%;
  padding: 10px;
  height: fit-content;
}
</style>
