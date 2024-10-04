<template>
  <v-dialog max-width="600px" v-model="mostrar" persistent>
    <v-card>
      <v-card-title class="d-flex justify-space-between">
        <span class="headline">{{ tituloFormulario }}</span>
        <v-btn aria-label="cerrar" icon @click="cerrarDialogo" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <v-form ref="form" @submit.prevent="guardarEjercicio">
          <div class="contenedor-flex">
            <div class="item-flex">
              <v-text-field
                class="placeholder"
                placeholder="Extensionde de brazos"
                v-model="ejercicioNuevo.nombre"
                label="Nombre"
                :rules="[(v) => !!v || 'El nombre es obligatorio']"
                required
              >
              </v-text-field>
            </div>
            <div class="item-flex">
              <v-textarea
                class="placeholder"
                placeholder="Descripción del ejercicio"
                v-model="ejercicioNuevo.descripcion"
                label="Descripción"
                :rules="[(v) => !!v || 'La descripción es obligatoria']"
                required
              >
              </v-textarea>
            </div>
            <div class="item-flex">
              <v-select
                class="placeholder"
                placeholder="Musculos principales"
                v-model="ejercicioNuevo.musculosPrincipales"
                :items="musculosItems"
                label="Músculos principales"
                multiple
              >
              </v-select>
            </div>
            <div class="item-flex">
              <v-select
                class="placeholder"
                placeholder="Musculos secundarios"
                v-model="ejercicioNuevo.musculosSecundarios"
                :items="musculosItems"
                label="Músculos secundarios"
                multiple
              >
              </v-select>
            </div>
            <div class="item-flex">
              <v-select
                class="placeholder"
                placeholder="Cualidad física"
                v-model="ejercicioNuevo.cualidad"
                :items="cualidadesItems"
                label="Cualidad"
                required
              >
              </v-select>
            </div>
            <div class="item-flex">
              <v-text-field
                class="placeholder"
                placeholder="Ejecutantes"
                v-model="ejercicioNuevo.numeroEjecutantes"
                label="Número de ejecutantes"
                type="number"
              >
              </v-text-field>
            </div>
            <div class="item-flex">
              <v-select
                class="placeholder"
                placeholder="Equipamiento necesario"
                v-model="ejercicioNuevo.equipamiento"
                :items="equipamientosRegistrados"
                item-title="nombre"
                item-value="_links.self.href"
                label="Equipamiento"
              >
              </v-select>
            </div>
            <div class="item-flex">
              <v-text-field
                class="placeholder"
                placeholder="Activo libre"
                v-model="ejercicioNuevo.tipoMovimiento"
                label="Tipo de movimiento"
              >
              </v-text-field>
            </div>
            <div class="item-flex">
              <v-text-field
                class="placeholder"
                placeholder="Isotónico concéntrico"
                v-model="ejercicioNuevo.tipoContraccion"
                label="Tipo de contracción"
              >
              </v-text-field>
            </div>
            <div class="item-flex">
              <v-text-field
                class="placeholder"
                placeholder="Normal"
                v-model="ejercicioNuevo.velocidad"
                label="Velocidad"
              >
              </v-text-field>
            </div>
            <div class="item-flex">
              <v-text-field
                class="placeholder"
                placeholder="Enlace a video"
                v-model="ejercicioNuevo.url"
                label="URL"
              >
              </v-text-field>
            </div>
            <div class="item-flex">
              <v-select
                class="placeholder"
                placeholder="1 RM"
                v-model="ejercicioNuevo.tipoCarga"
                :items="tipoCargaItems"
                label="Carga"
                item-title="texto"
                item-value="valor"
                :rules="[(v) => !!v || 'El tipo de carga es obligatorio']"
                required
              >
              </v-select>
            </div>
          </div>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn aria-label="cerrar" color="primary" text @click="cerrarDialogo"
              >Cancelar</v-btn
            >
            <v-btn aria-label="guardar" color="primary" type="submit">Guardar</v-btn>
          </v-card-actions>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import musculos from "../assets/musculos.json";
import { useEquipamientosStore } from "@/store/equipamientosStore";
import { mapState, mapActions } from "pinia";

export default {
  props: {
    ejercicio: {
      type: Object,
    },
    edicion: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    ...mapState(useEquipamientosStore, ["equipamientosRegistrados"]),
  },
  data() {
    return {
      tipoCargaItems: [
        { texto: "Repeticiones", valor: "REPS" },
        { texto: "1 RM", valor: "RM" },
        { texto: "VAM", valor: "VAM" },
      ],
      cualidadesItems: [
        "Fuerza",
        "Resistencia",
        "Coordinación",
        "Flexibilidad",
      ],
      tituloFormulario: this.edicion ? "Editar Ejercicio" : "Crear Ejercicio",
      musculosItems: musculos,
      mostrar: true,
      ejercicioNuevo: {
        nombre: "",
        descripcion: "",
        cualidad: "",
        numeroEjecutantes: 0,
        equipamiento: "",
        tipoMovimiento: "",
        tipoContraccion: "",
        velocidad: "",
        url: "",
        tipoCarga: "",
        musculosPrincipales: [],
        musculosSecundarios: [],
      },
    };
  },
  watch: {
    edicion(nuevoValor) {
      this.tituloFormulario = nuevoValor
        ? "Editar Ejercicio"
        : "Crear Ejercicio";
    },
    ejercicio: {
      immediate: true,
      handler(nuevoValor) {
        this.ejercicioNuevo = { ...nuevoValor };
      },
    },
  },
  methods: {
    ...mapActions(useEquipamientosStore, ["cargarEquipamientos"]),
    cerrarDialogo() {
      this.$emit("cerrar");
      this.resetForm();
    },
    resetForm() {
      this.ejercicioNuevo = { ...this.ejercicio };
      this.ejercicioNuevo.equipamiento = "";
    },
    async guardarEjercicio() {
      let isValid = await this.$refs.form.validate();
      if (isValid.valid) {
        this.$emit("guardar", this.ejercicioNuevo);
        this.cerrarDialogo();
      }
    },
  },
  async created() {
    this.resetForm();
    await this.cargarEquipamientos();
    this.ejercicioNuevo.numeroEjecutantes = 1;
  },
};
</script>

<style scoped>
.headline {
  margin-top: 2vh;
}

.contenedor-flex {
  display: flex;
  flex-direction: column;
  gap: 16px;
  align-items: stretch;
}

.item-flex {
  display: flex;
  flex-direction: column;
}
</style>
