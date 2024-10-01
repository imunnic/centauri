<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="flex-fila justify-space-between">
        <span>Crear Nueva Sesión</span>
        <v-btn icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <form @submit.prevent="submitForm" class="contenedor-flex">
          <v-text-field
            label="Nombre de la Sesión"
            v-model="nombre"
            required
            class="item-flex"
          ></v-text-field>
          <v-select
            label="Grupo"
            v-model="grupo"
            :items="grupos"
            item-title="nombre"
            item-value="nombre"
            required
            class="item-flex"
          ></v-select>
          <v-select
            label="Unidad"
            v-model="unidad"
            :items="unidadesRegistradas"
            class="item-flex"
          ></v-select>
          <v-text-field
            label="Fecha"
            v-model="fechaSesion"
            type="date"
            required
            class="item-flex"
          ></v-text-field>
          <v-select
            label="Calentamiento"
            v-model="calentamiento"
            :items="fichasCalentamiento"
            item-title="nombre"
            item-value="id"
            multiple
            class="item-flex"
          ></v-select>
          <v-select
            label="Parte Fundamental"
            v-model="fundamental"
            :items="fichasFundamental"
            item-title="nombre"
            item-value="id"
            multiple
            class="item-flex"
          ></v-select>
          <v-select
            label="Coordinación funcional"
            v-model="coordinacion"
            :items="fichasCoordinacion"
            item-title="nombre"
            item-value="id"
            multiple
            class="item-flex"
          ></v-select>
          <v-select
            label="Vuelta a la calma"
            v-model="vueltaCalma"
            :items="fichasCalma"
            item-title="nombre"
            item-value="id"
            multiple
            class="item-flex"
          ></v-select>
          <v-btn type="submit" class="claro">Crear Sesión</v-btn>
        </form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import unidades from "@/assets/unidades.json";
import configuracion from "@/configuracion.json";
import { useFichasStore } from "@/store/fichasStore.js";
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
  },
  computed: {
    ...mapState(useFichasStore, ["fichasRegistradas"]),
  },
  data() {
    return {
      dialog: true,
      nombre: "",
      grupo: "",
      unidad: "",
      fechaSesion: "",
      unidadesRegistradas: unidades,
      calentamiento: [],
      fundamental: [],
      coordinacion: [],
      vueltaCalma: [],
      fichasCalentamiento: [],
      fichasFundamental: [],
      fichasCoordinacion: [],
      fichasCalma: [],
    };
  },
  methods: {
    ...mapActions(useFichasStore, ["cargarFichas"]),
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
    submitForm() {
      const fichasFundamentalHref = this.fundamental.map(
        (ficha) => configuracion.urlBase + "fichas/" + ficha
      );
      const fichasCalentamientoHref = this.calentamiento.map(
        (ficha) => configuracion.urlBase + "fichas/" + ficha
      );
      const fichasCoordinacionHref = this.coordinacion.map(
        (ficha) => configuracion.urlBase + "fichas/" + ficha
      );
      const fichasCalmaHref = this.vueltaCalma.map(
        (ficha) => configuracion.urlBase + "fichas/" + ficha
      );
      const nuevaSesion = {
        nombre: this.nombre,
        grupo: this.grupo.nombre,
        unidad: this.unidad,
        fecha: this.fechaSesion,
        fichas: [
          ...fichasCalentamientoHref,
          ...fichasFundamentalHref,
          ...fichasCoordinacionHref,
          ...fichasCalmaHref,
        ],
      };
      this.$emit("sesionCreada", nuevaSesion);
      this.resetForm();
      this.cerrar();
    },
    resetForm() {
      this.nombre = "";
      this.grupo = "";
      this.fechaSesion = "";
    },
    formatoFecha(fecha) {
      const dia = String(fecha.getDate()).padStart(2, "0");
      const mes = String(fecha.getMonth() + 1).padStart(2, "0");
      const ano = String(fecha.getFullYear());

      return `${ano}-${mes}-${dia}`;
    },
  },
  async created() {
    this.fechaSesion = this.formatoFecha(this.fecha);
    this.grupo = this.grupos[0];
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
