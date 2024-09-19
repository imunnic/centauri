<template>
  <v-card class="contenedor" elevation="3">
    <div class="contenedor-flex flex-columna">
      <v-card-title class="titulo-serie">Serie de {{ serie.ejercicio.nombre }}</v-card-title>
      <div v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'REPS'" class="flex-fila">
        <div class="info-serie">
          <p class="definicion-serie"><b>Carga: {{ serie.carga }} %</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Reps'"> <b>{{ marcaEjercicio }} repeticiones</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Tiempo'"><b>{{ marcaEjercicio }} minutos</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Dist'"><b>{{ marcaEjercicio }} metros</b></p>
        </div>
        <div class="info-serie">
          <div class="flex-item">
            <div class="input-label">Marca Objetivo</div>
            <v-text-field v-model.number="marcaObjetivo" :label="'Marca'" type="number" min="0" step="1"
              class="input-corto"></v-text-field>
          </div>
        </div>
      </div>
      <div v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'VAM'" class="flex-fila">
        <div class="info-serie">
          <p class="definicion-serie"><b>Carga: {{ serie.carga }} %</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Tiempo'"><b>{{ formattedDuracion }} minutos a {{ marcaEjercicio }}</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Dist'"><b>{{ formattedDuracion }} metros a {{ marcaEjercicio }}</b></p>
        </div>
        <div class="info-serie">
          <div class="flex-item">
            <div class="input-label">Marca Objetivo</div>
            <v-text-field
              v-model="marcaFormatted"
              :label="'Ritmo'"
              type="text"
              class="input-corto"
              @input="onInput"
            ></v-text-field>
          </div>
        </div>
      </div>
      <div v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'RM'" class="flex-fila">
        <div class="info-serie">
          <p class="definicion-serie"><b>Carga: {{ serie.carga }} %</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Reps'"><b>{{ formattedDuracion }} repeticiones con {{ marcaEjercicio }} kg</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Tiempo'"><b>{{ formattedDuracion }} minutos con {{ marcaEjercicio }} kg</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'Dist'"><b>{{ formattedDuracion }} metros con {{ marcaEjercicio }} kg</b></p>
        </div>
        <div class="info-serie">
          <div class="flex-item">
            <div class="input-label">Marca Objetivo</div>
            <v-text-field v-model.number="marcaObjetivo" :label="'Marca'" type="number" min="0" step="1"
              class="input-corto"></v-text-field>
          </div>
        </div>
      </div>
      <div v-if="serie.ajustable == false">
        <p class="definicion-serie" v-if="serie.tipo == 'Reps'"> <b>{{ formattedDuracion }} repeticiones</b></p>
        <p class="definicion-serie" v-if="serie.tipo == 'Tiempo'"><b>{{ formattedDuracion }} minutos</b></p>
        <p class="definicion-serie" v-if="serie.tipo == 'Dist'"><b>{{ formattedDuracion }} metros</b></p>
      </div>
    </div>
  </v-card>
</template>


<script>
import { useEjerciciosStore } from '../store/ejerciciosStore.js';
import { mapState } from 'pinia';

export default {
  props: {
    serie: {
      type: Object,
      default: () => ({
        ejercicio: this.ejerciciosRegistrados[0],
        tipo: 'Reps',
        cantidad: 1,
        peso: 0,
        ajustable: false
      })
    },
    soloLectura: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      marcaObjetivo: 10,
      ritmoObjetivo: 240
    };
  },
  computed: {
    ...mapState(useEjerciciosStore, ['ejerciciosRegistrados']),

    marcaFormatted() {
      const minutes = Math.floor(this.ritmoObjetivo / 60).toString().padStart(2, '0');
      const seconds = (this.ritmoObjetivo % 60).toString().padStart(2, '0');
      return `${minutes}:${seconds}`;
    },
    
    formattedTime() {
      const minutes = Math.floor(this.serie.cantidad / 60).toString().padStart(2, '0');
      const seconds = (this.serie.cantidad % 60).toString().padStart(2, '0');
      return `${minutes}:${seconds}`;
    },
    
    formattedDuracion() {
      if (this.serie.tipo === 'Tiempo') {
        return this.formattedTime;
      }
      return `${this.serie.cantidad}`;
    },
    
    marcaEjercicio() {
      if (this.serie.ejercicio.tipoCarga === 'VAM') {
        const ritmoEnSegundos = this.ritmoObjetivo + (this.ritmoObjetivo * ((100 - this.serie.carga) / 100));
        const minutos = Math.floor(ritmoEnSegundos / 60).toString().padStart(2, '0');
        const segundos = (ritmoEnSegundos % 60).toFixed(0).toString().padStart(2, '0');
        return `${minutos}:${segundos} min/km`;
      } else {
        return (this.serie.carga * this.marcaObjetivo * 0.01).toFixed(0);
      }
    }
  },
  methods: {
    onInput(value) {
      const [minutes, seconds] = value.split(':').map(Number);
      if (!isNaN(minutes) && !isNaN(seconds)) {
        this.marcaObjetivo = (minutes * 60) + seconds;
      }
    },
    quitarSerie() {
      this.$emit('quitar-serie', this.serie.id);
    }
  },
  mounted() {
    if (!this.soloLectura) {
      this.serie.tipo = 'Reps';
      this.serie.cantidad = 1;
    }
  }
};
</script>

<style scoped>
.titulo-serie {
  font-size: 20px;
  font-weight: 600;
  text-decoration: underline;
}

.definicion-serie {
  padding-left: 15px;
  font-size: 20px;
  margin-bottom: 16px;
}

.info-serie {
  margin: 10px;
}

.contenedor-flex {
  align-items: start;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 8px;
}

.flex-item {
  flex: 1;
  min-width: 200px;
}

.input-corto {
  flex: 1;
}

.input-label {
  font-weight: bold;
  margin-bottom: 8px;
}

.carga {
  margin-top: 16px;
  width: auto;
  align-items: flex-start;
}

@media (max-width: 600px) {
  .contenedor-flex {
    flex-direction: column;
  }

  .flex-item {
    min-width: unset;
  }
}
</style>