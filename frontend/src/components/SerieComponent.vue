<template>
  <v-expansion-panels v-if="soloLectura == false">
    <v-expansion-panel :hide-actions="soloLectura" class="carta contenedor">
      <v-expansion-panel-title class="componente" style="position: relative;">
        <div class="contenedor">
          <div>
            <p class="definicion-serie" v-if="serie.tipo == 'REPS'"> <b>{{ formatoDuracion }} repeticiones de {{
    ejercicioSeleccionadoNombre }}</b></p>
            <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'"><b>{{ formatoDuracion }} minutos de {{
    ejercicioSeleccionadoNombre }}</b></p>
            <p class="definicion-serie" v-if="serie.tipo == 'DIST'"><b>{{ formatoDuracion }} metros de {{
    ejercicioSeleccionadoNombre }}</b></p>
          </div>
          <div v-if="serie.ajustable">
            <p class="definicion-serie"><b>{{ serie.carga }} % de carga</b></p>
          </div>
          <div class="info">
          </div>
        </div>
      </v-expansion-panel-title>
      <v-expansion-panel-text v-if="soloLectura == false" style="position: relative;">
        <v-form class="contenedor">
          <v-select v-model="serie.ejercicio" label="Ejercicio" :items="ejerciciosRegistrados" item-title="nombre"
            item-value="_links.self.href" required :disabled="soloLectura"></v-select>
          <div class="flex-container">
            <div class="input-medio">
              <v-select v-model="serie.tipo" :items="['REPS', 'TIEMPO', 'DIST']" label="Tipo" required
                :disabled="soloLectura"></v-select>
            </div>
            <div class="input-corto">
              <v-text-field v-if="serie.tipo === 'REPS'" v-model="serie.cantidad" label="Repeticiones" type="number"
                required :disabled="soloLectura"></v-text-field>
              <v-text-field v-if="serie.tipo === 'TIEMPO'" v-model="formatoTiempo" label="Tiempo" type="time" required
                :disabled="soloLectura"></v-text-field>
              <v-text-field v-if="serie.tipo === 'DIST'" v-model="serie.cantidad" label="Distancia" type="number"
                required :disabled="soloLectura"></v-text-field>
            </div>
          </div>
          <v-text-field v-if="serie.ajustable" v-model="serie.carga" label="Carga (%)" type="number" required class="input-corto"
            :disabled="soloLectura"></v-text-field>
          <v-checkbox v-model="serie.ajustable" label="Adaptable" :disabled="soloLectura"></v-checkbox>
        </v-form>
      </v-expansion-panel-text>
    </v-expansion-panel>
    <v-icon color="error" @click.stop="quitarSerie" style="position: absolute; top: 0; right: 0; margin: 8px;"
      v-if="!soloLectura">
      mdi-trash-can
    </v-icon>
  </v-expansion-panels>
  <v-card v-else class="contenedor" elevation="3">
    <div class="contenedor-flex flex-columna">
      <v-card-title class="titulo-serie">Serie de {{ serie.ejercicio.nombre }}</v-card-title>
      <div v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'REPS'" class="flex-fila">
        <div class="info-serie">
          <p class="definicion-serie"><b>Carga: {{ serie.carga }} %</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'REPS'"> <b>{{ marcaEjercicio }} repeticiones</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'"><b>{{ marcaEjercicio }} minutos</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'DIST'"><b>{{ marcaEjercicio }} metros</b></p>
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
          <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
            <b>{{ formatoDuracion }} minutos a {{ marcaEjercicio }}</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
            <b>{{ formatoDuracion }} metros a {{ marcaEjercicio }}</b>
          </p>
        </div>
        <div class="info-serie">
          <div class="flex-item">
            <div class="input-label">Marca Objetivo</div>
            <v-text-field v-model="formatoMarca" :label="'Ritmo'" type="text" class="input-corto"
              @input="onInput"></v-text-field>
          </div>
        </div>
      </div>
      <div v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'RM'" class="flex-fila">
        <div class="info-serie">
          <p class="definicion-serie"><b>Carga: {{ serie.carga }} %</b></p>
          <p class="definicion-serie" v-if="serie.tipo == 'REPS'">
            <b>{{ formatoDuracion }} repeticiones con {{ marcaEjercicio }} kg</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
            <b>{{ formatoDuracion }} minutos con {{ marcaEjercicio }} kg</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
            <b>{{ formatoDuracion }} metros con {{ marcaEjercicio }} kg</b>
          </p>
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
        <p class="definicion-serie" v-if="serie.tipo == 'REPS'"> <b>{{ formatoDuracion }} repeticiones</b></p>
        <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'"><b>{{ formatoDuracion }} minutos</b></p>
        <p class="definicion-serie" v-if="serie.tipo == 'DIST'"><b>{{ formatoDuracion }} metros</b></p>
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
        tipo: 'REPS',
        cantidad: 1,
        peso: 0,
        ajustable: true
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
    ejercicioSeleccionadoNombre() {
      const ejercicioSeleccionado = this.ejerciciosRegistrados.find(
        ejercicio => ejercicio._links.self.href === this.serie.ejercicio
      );
      return ejercicioSeleccionado ? ejercicioSeleccionado.nombre : 'Ejercicio no seleccionado';
    },
    formatoMarca() {
      const minutes = Math.floor(this.ritmoObjetivo / 60).toString().padStart(2, '0');
      const seconds = (this.ritmoObjetivo % 60).toString().padStart(2, '0');
      return `${minutes}:${seconds}`;
    },

    formatoTiempo() {
      const minutes = Math.floor(this.serie.cantidad / 60).toString().padStart(2, '0');
      const seconds = (this.serie.cantidad % 60).toString().padStart(2, '0');
      return `${minutes}:${seconds}`;
    },

    formatoDuracion() {
      if (this.serie.tipo === 'TIEMPO') {
        return this.formatoTiempo;
      }
      return `${this.serie.cantidad}`;
    },

    marcaEjercicio() {
      if (this.serie.ejercicio.tipoCarga === 'VAM') {
        const ritmoEnSegundos =
          this.ritmoObjetivo + (this.ritmoObjetivo * ((100 - this.serie.carga) / 100));
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
  created() { },
  mounted() {
    if (!this.soloLectura) {
      this.serie.tipo = 'REPS';
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