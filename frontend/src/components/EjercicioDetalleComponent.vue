<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="d-flex justify-space-between">
        <span>{{ ejercicio.nombre }}</span>
        <v-btn icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-subtitle>
        <v-icon class="texto" v-if="ejercicio.numeroEjecutantes === 1">mdi-account</v-icon>
        <div class="texto" v-if="ejercicio.numeroEjecutantes === 2">
          <v-icon>mdi-account</v-icon>
          <v-icon>mdi-account</v-icon>
        </div>
        <v-icon class="texto" v-if="ejercicio.numeroEjecutantes > 2">mdi-account-group</v-icon>
      </v-card-subtitle>
      <v-card-text>
        <iframe
          v-if="ejercicio.url"
          :src="formatoUrl(ejercicio.url)"
          frameborder="0"
          allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
          class="iframe-centrado"
        ></iframe>
        <div v-else class="contenedor-imagen">
          <p class="mensaje-video-no-disponible">No hay video disponible</p>
          <img :src="videoNoDisponible" alt="Video no disponible" class="imagen-ajustada">
        </div>
        <p><b>Cualidad</b>: {{ ejercicio.cualidad || 'No especificado' }}</p>
        <p><b>Tipo de movimiento</b>: {{ ejercicio.tipoMovimiento || 'No especificado' }}</p>
        <p><b>Tipo de contracción</b>: {{ ejercicio.tipoContraccion || 'No especificado' }}</p>
        <p><b>Velocidad</b>: {{ ejercicio.velocidad || 'No especificado' }}</p>
        <p><b>Equipamiento</b>: {{ equipamiento.nombre || 'No especificado' }}</p>
        <p><b>Músculos principales</b>: {{ formatoLista(ejercicio.musculosPrincipales) }}</p>
        <p><b>Músculos secundarios</b>: {{ formatoLista(ejercicio.musculosSecundarios) }}</p>
        <p><b>Tipo de Carga</b>: {{ tipoCargaDisplay }}</p>
        <p><b>Descripción</b>: {{ ejercicio.descripcion }}</p>
      </v-card-text>
      <v-card-actions class="d-flex justify-center margen"></v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import configuracion from '@/configuracion.json';
import { useEjerciciosStore } from '@/store/ejerciciosStore.js'
import { mapActions } from 'pinia'

export default {
  props: {
    ejercicio: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      dialog: true,
      equipamiento: {},
      videoNoDisponible: configuracion.urlVideoNoEncontrado
    };
  },
  computed: {
    tipoCargaDisplay() {
      switch (this.ejercicio.tipoCarga) {
        case 'REPS':
          return 'Repeticiones';
        case 'RM':
          return '1 RM';
        case 'VAM':
          return 'VAM';
        default:
          return 'Tipo de carga no especificado';
      }
    }
  },
  methods: {
    ...mapActions(useEjerciciosStore, ['getEquipamientoDeEjercicio']),
    formatoUrl(url) {
      return url.replace('watch?v=', 'embed/');
    },
    cerrar() {
      this.dialog = false;
      this.$emit('cerrar');
    },
    formatoLista(lista) {
      return Array.isArray(lista) && lista.length > 0 ? lista.join(', ') : 'Ninguno';
    },
  },
  async created() {
    let response = await this.getEquipamientoDeEjercicio(this.ejercicio);
    this.equipamiento = response.data;
  }
}
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
