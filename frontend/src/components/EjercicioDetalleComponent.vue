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
        <p><b>Cualidad</b>: {{ ejercicio.cualidad || 'No especificado' }}</p>
        <p><b>Tipo de movimiento</b>: {{ ejercicio.tipoMovimiento || 'No especificado' }}</p>
        <p><b>Tipo de contracción</b>: {{ ejercicio.tipoContraccion || 'No especificado' }}</p>
        <p><b>Velocidad</b>: {{ ejercicio.velocidad || 'No especificado' }}</p>
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
      equipo: null
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
    // Código adicional si es necesario
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
</style>
