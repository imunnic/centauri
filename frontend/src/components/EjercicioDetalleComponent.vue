<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <span>{{ ejercicio.nombre }}</span>
        <v-btn aria-label="cerrar" icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-subtitle>
        <v-icon class="texto" v-if="ejercicio.numeroEjecutantes === 1"
          >mdi-account</v-icon
        >
        <div class="texto" v-if="ejercicio.numeroEjecutantes === 2">
          <v-icon>mdi-account</v-icon>
          <v-icon>mdi-account</v-icon>
        </div>
        <v-icon class="texto" v-if="ejercicio.numeroEjecutantes > 2"
          >mdi-account-group</v-icon
        >
      </v-card-subtitle>
      <v-card-text>
        <LiteYouTubeEmbed v-if="ejercicio.url" :id="videoId(ejercicio.url)"></LiteYouTubeEmbed>

        <div v-else class="contenedor-imagen">
          <p class="mensaje-video-no-disponible">No hay video disponible</p>
          <img
            :src="videoNoDisponible"
            alt="Video no disponible"
            class="imagen-ajustada"
          />
        </div>
        <p><b>Cualidad</b>: {{ ejercicio.cualidad || "No especificado." }}</p>
        <p>
          <b>Tipo de movimiento</b>:
          {{ ejercicio.tipoMovimiento || "No especificado." }}
        </p>
        <p>
          <b>Tipo de contracción</b>:
          {{ ejercicio.tipoContraccion || "No especificado." }}
        </p>
        <p><b>Velocidad de ejecución</b>: {{ ejercicio.velocidad || "No especificado." }}</p>
        <p>
          <b>Equipamiento</b>: {{ equipamiento.nombre || "No especificado." }}
        </p>
        <p>
          <b>Músculos principales</b>:
          {{ formatoLista(ejercicio.musculosPrincipales) }}
        </p>
        <p>
          <b>Músculos secundarios</b>:
          {{ formatoLista(ejercicio.musculosSecundarios) }}
        </p>
        <p><b>Tipo de Carga</b>: {{ tipoCargaDisplay }}</p>
        <p><b>Descripción</b>: {{ ejercicio.descripcion }}</p>
      </v-card-text>
      <v-card-actions class="d-flex justify-center margen"></v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import configuracion from "@/configuracion.json";
import LiteYouTubeEmbed from 'vue-lite-youtube-embed'
import 'vue-lite-youtube-embed/style.css'
import { useEjerciciosStore } from "@/store/ejerciciosStore.js";
import { mapActions } from "pinia";

export default {
  components:{LiteYouTubeEmbed},
  props: {
    ejercicio: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      dialog: true,
      equipamiento: { nombre: "No especificado." },
      videoNoDisponible: configuracion.urlVideoNoEncontrado,
    };
  },
  computed: {
    tipoCargaDisplay() {
      switch (this.ejercicio.tipoCarga) {
        case "REPS":
          return "Repeticiones";
        case "RM":
          return "1 RM";
        case "VAM":
          return "VAM";
        default:
          return "Tipo de carga no especificado.";
      }
    },
  },
  methods: {
    ...mapActions(useEjerciciosStore, ["getEquipamientoDeEjercicio"]),
    videoId(url) {
      return url.split('=').pop().split("/").pop();
    },
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
    formatoLista(lista) {
      return Array.isArray(lista) && lista.length > 0
        ? lista.join(", ")
        : "Ninguno";
    },
  },
  async created() {
    try {
      let response = await this.getEquipamientoDeEjercicio(this.ejercicio);
      if (response && response.data) {
        this.equipamiento = response.data;
      } else {
        this.equipamiento = { nombre: "No especificado." };
      }
    } catch (error) {
      console.error("Error fetching equipamiento:", error);
      this.equipamiento = { nombre: "No especificado." };
    }
  },
};
</script>

<style scoped>
.texto {
  margin: 10px;
}

.margen {
  margin-bottom: 30px;
}

.iframe-centrado {
  display: block;
  margin: 20px auto;
  width: 100%;
  max-width: 560px;
  height: 315px;
}

.contenedor-imagen {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.mensaje-video-no-disponible {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 18px;
  color: white;
  background-color: rgba(0, 0, 0, 0.8);
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 2;
}

.imagen-ajustada {
  max-width: 30%;
  height: auto;
  object-fit: contain;
  z-index: 1;
}
</style>
