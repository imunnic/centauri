<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="flex-fila justify-space-between align-center">
        <span v-if="edicion">Editar Sesión</span>
        <span v-else>Crear Nueva Sesión</span>

        <v-btn aria-label="cerrar" icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <form @submit.prevent="submitForm" class="contenedor-flex">
          <v-text-field
            label="Nombre de la Sesión"
            v-model="nombre"
            :rules="[reglas.required]"
            required
            placeholder="Ingrese el nombre de la sesión"
            class="item-flex placeholder"
          ></v-text-field>
          <v-select
            label="Grupo"
            v-model="grupo"
            :items="grupos"
            item-title="nombre"
            item-value="nombre"
            :rules="[reglas.required]"
            required
            placeholder="Seleccione un grupo"
            class="item-flex placeholder"
          ></v-select>
          <v-autocomplete
            label="Unidad"
            v-model="unidad"
            :items="unidadesRegistradas"
            placeholder="Seleccione una unidad"
            class="item-flex placeholder"
          ></v-autocomplete>
          <v-text-field
            label="Fecha"
            v-model="fechaSesion"
            type="date"
            :rules="[reglas.required]"
            required
            placeholder="Seleccione una fecha"
            class="item-flex placeholder"
          ></v-text-field>
          <v-autocomplete
            label="Calentamiento"
            v-model="calentamiento"
            :items="fichasCalentamiento"
            item-title="nombre"
            item-value="id"
            multiple
            placeholder="Seleccione calentamientos"
            class="item-flex placeholder"
          ></v-autocomplete>
          <v-autocomplete
            label="Parte Fundamental"
            v-model="fundamental"
            :items="fichasFundamental"
            item-title="nombre"
            item-value="id"
            multiple
            placeholder="Seleccione partes fundamentales"
            @update:model-value="validar"
            class="item-flex placeholder"
          ></v-autocomplete>
          <v-alert
            v-if="!fichasSeleccionadasAdecuadas"
            type="warning"
            class="mt-3"
          >
            {{ mensajeAlerta }}
          </v-alert>
          <v-autocomplete
            label="Coordinación funcional"
            v-model="coordinacion"
            :items="fichasCoordinacion"
            item-title="nombre"
            item-value="id"
            multiple
            placeholder="Seleccione coordinación funcional"
            class="item-flex placeholder"
          ></v-autocomplete>
          <v-autocomplete
            label="Vuelta a la calma"
            v-model="vueltaCalma"
            :items="fichasCalma"
            item-title="nombre"
            item-value="id"
            multiple
            placeholder="Seleccione vuelta a la calma"
            class="item-flex placeholder"
          ></v-autocomplete>
          <v-btn
            aria-label="crear-sesion"
            v-if="!edicion"
            type="submit"
            class="claro"
            >Crear Sesión</v-btn
          >
          <v-btn aria-label="editar-sesion" v-else type="submit" class="claro"
            >Editar Sesión</v-btn
          >
        </form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import unidades from "@/assets/unidades.json";
import configuracion from "@/configuracion.json";
import { useFichasStore } from "@/store/fichasStore.js";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { mapState, mapActions } from "pinia";

export default {
  props: {
    grupos: {
      type: Array,
      required: true,
    },
    fecha: {
      type: Date,
      required: false,
    },
    sesion: {
      type: Object,
      required: false,
    },
    edicion: {
      type: Boolean,
      required: true,
    },
  },
  computed: {
    ...mapState(useFichasStore, ["fichasRegistradas"]),
    mensajeAlerta() {
      switch (this.tipoAlerta) {
        case 0:
          return "";
        case 1:
          return "La carga de la ficha seleccionada es demasiado elevada.";
        case 2:
          return "La sesión anterior tiene una carga elevada.";
        case 3:
          return "La media de esfuerzo estimada de las fichas es demasiado elevada.";
        case 4:
          return "La media de esfuerzo con la sesión anterior es demasiado elevada.";
        default:
          return "";
      }
    },
  },
  data() {
    return {
      dialog: true,
      nombre: this.sesion.nombre || "",
      grupo: this.sesion.grupo || "",
      unidad: this.sesion.unidad || "",
      fechaSesion: "",
      fichasSeleccionadasAdecuadas: true,
      tipoAlerta: 0,
      unidadesRegistradas: unidades,
      calentamiento: [],
      fundamental: [],
      coordinacion: [],
      vueltaCalma: [],
      fichasCalentamiento: [],
      fichasFundamental: [],
      fichasCoordinacion: [],
      fichasCalma: [],
      reglas: {
        required: (value) => !!value || "Este campo es requerido",
      },
    };
  },
  methods: {
    ...mapActions(useFichasStore, ["cargarFichas"]),
    ...mapActions(useSesionesStore, ["validarFichasSesion"]),
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
    submitForm() {
      const fichasFundamentalHref = this.fundamental.map(
        (ficha) =>
          configuracion.urlBase +
          "fichas/" +
          (typeof ficha === "object" ? ficha.id : ficha)
      );
      const fichasCalentamientoHref = this.calentamiento.map(
        (ficha) =>
          configuracion.urlBase +
          "fichas/" +
          (typeof ficha === "object" ? ficha.id : ficha)
      );
      const fichasCoordinacionHref = this.coordinacion.map(
        (ficha) =>
          configuracion.urlBase +
          "fichas/" +
          (typeof ficha === "object" ? ficha.id : ficha)
      );
      const fichasCalmaHref = this.vueltaCalma.map(
        (ficha) =>
          configuracion.urlBase +
          "fichas/" +
          (typeof ficha === "object" ? ficha.id : ficha)
      );
      let nuevaSesion = {
        nombre: this.nombre,
        grupo: this.grupo,
        unidad: this.unidad,
        fecha: this.fechaSesion,
        fichas: [
          ...fichasCalentamientoHref,
          ...fichasFundamentalHref,
          ...fichasCoordinacionHref,
          ...fichasCalmaHref,
        ],
      };
      if (this.edicion) {
        nuevaSesion.href = this.sesion.href;
        this.$emit("sesionEditada", nuevaSesion);
      } else {
        this.$emit("sesionCreada", nuevaSesion);
      }
      this.resetForm();
      this.cerrar();
    },
    resetForm() {
      this.nombre = "";
      this.grupo = "";
      this.unidad = "";
      this.fechaSesion = "";
      this.calentamiento = [];
      this.fundamental = [];
      this.coordinacion = [];
      this.vueltaCalma = [];
    },
    formatoFecha(fecha) {
      const dia = String(fecha.getDate()).padStart(2, "0");
      const mes = String(fecha.getMonth() + 1).padStart(2, "0");
      const ano = String(fecha.getFullYear());

      return `${ano}-${mes}-${dia}`;
    },
    cargarFichasSeleccionadas() {
      const fichasSesion = this.sesion.fichas;
      const hrefFichasSesion = fichasSesion.map(
        (ficha) => ficha._links.self.href
      );
      const idFichasSesion = hrefFichasSesion.map((ficha) =>
        ficha.split("/").pop()
      );

      this.calentamiento = this.fichasCalentamiento.filter((ficha) =>
        idFichasSesion.includes(ficha.id)
      );

      this.fundamental = this.fichasFundamental.filter((ficha) =>
        idFichasSesion.includes(ficha.id)
      );

      this.coordinacion = this.fichasCoordinacion.filter((ficha) =>
        idFichasSesion.includes(ficha.id)
      );

      this.vueltaCalma = this.fichasCalma.filter((ficha) =>
        idFichasSesion.includes(ficha.id)
      );
    },
    async validar() {
      let nuevaSesion = {
        grupo: this.grupo.nombre,
        fecha: this.fechaSesion,
        fichas: [...this.fundamental],
      };
      nuevaSesion.fichas = nuevaSesion.fichas.map((ficha) => ({ id: ficha }));
      this.tipoAlerta = await this.validarFichasSesion(nuevaSesion);
      if (this.tipoAlerta == 0) {
        this.fichasSeleccionadasAdecuadas = true;
      } else {
        this.fichasSeleccionadasAdecuadas = false;
      }
    },
  },
  async created() {
    await this.cargarFichas();
    this.fichasCalentamiento = this.fichasRegistradas.filter(
      (ficha) => ficha.parteSesion == "CALENTAMIENTO"
    );
    this.fichasFundamental = this.fichasRegistradas.filter(
      (ficha) => ficha.parteSesion == "FUNDAMENTAL"
    );
    this.fichasCoordinacion = this.fichasRegistradas.filter(
      (ficha) => ficha.parteSesion == "COORDINACION"
    );
    this.fichasCalma = this.fichasRegistradas.filter(
      (ficha) => ficha.parteSesion == "CALMA"
    );
    if (this.edicion) {
      this.fechaSesion = this.formatoFecha(this.fecha);
      this.cargarFichasSeleccionadas();
    } else {
      this.fechaSesion = this.formatoFecha(this.fecha);
    }
  },
};
</script>

<style scoped>
.formulario-sesion {
  margin-top: 20px;
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
