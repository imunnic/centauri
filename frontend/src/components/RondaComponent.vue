<template>
  <v-card elevation="2" class="ronda-componente contenedor carta">
    <div class="ronda-header">
      <div class="contenedor-input" v-if="!soloLectura">
        <v-select v-model="ronda.tipo" :items="['REPS', 'TIEMPO']" label="Tipo" required class="input-medio"
          :disabled="soloLectura"></v-select>
        <v-text-field v-if="ronda.tipo === 'REPS'" v-model="ronda.cantidad" label="Cantidad" type="number" required
          class="input-corto" :disabled="soloLectura"></v-text-field>
        <v-text-field v-if="ronda.tipo === 'TIEMPO'" v-model="formatoTiempo" label="TIEMPO" type="time" required
          class="input-corto" :disabled="soloLectura"></v-text-field>
      </div>
      <div v-else class="flex-fila">
        <p class="definicion-ronda" v-if="ronda.tipo == 'REPS'"> <b>{{ ronda.cantidad }} repeticiones</b></p>
        <p class="definicion-ronda" v-if="ronda.tipo == 'TIEMPO'"><b>Ronda de {{ formatoTiempo }} minutos</b></p>
      </div>

      <v-icon small @click="quitarRonda" color="error" class="icono-papelera" 
      v-if="!soloLectura">mdi-trash-can</v-icon>
    </div>

    <div class="contenedor-flex">
      <div class="flex-item justify-start">
        <span class="info-titulo">Series:</span>
      </div>
      <div class="flex-item justify-end">
        <v-btn icon @click.stop="agregarSerie" class="claro" size="x-small" v-if="!soloLectura">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </div>
    </div>

    <draggable v-model="ronda.series" class="lista-series" :item-key="getItemKey" :disabled="soloLectura">
      <template #item="{ element, index }">
        <SerieComponent class="carta" :key="element.id" :serie="element" @quitar-serie="quitarSerie"
          @update-serie="actualizarSeries" :solo-lectura="soloLectura" />
      </template>
    </draggable>
  </v-card>
</template>

<script>
import { useEjerciciosStore } from '../store/ejerciciosStore.js';
import { mapState } from 'pinia';
import { v4 as uuidv4 } from 'uuid';
import SerieComponent from '../components/SerieComponent.vue';
import draggable from 'vuedraggable';

export default {
  props: {
    ronda: {
      type: Object,
      required: true
    },
    soloLectura: {
      type: Boolean,
      default: false
    }
  },
  components: {
    SerieComponent,
    draggable
  },
  computed: {
    ...mapState(useEjerciciosStore, ['ejercicioVacio']),
    formatoTiempo: {
      get() {
        const minutes = Math.floor(this.ronda.cantidad / 60).toString().padStart(2, '0');
        const seconds = (this.ronda.cantidad % 60).toString().padStart(2, '0');
        return `${minutes}:${seconds}`;
      },
      set(value) {
        const [minutes, seconds] = value.split(':').map(Number);
        this.ronda.cantidad = (minutes * 60) + seconds;
      }
    }
  },
  methods: {
    agregarSerie() {
      if (!this.soloLectura) {
        this.ronda.series
          .push({ id: uuidv4(), ejercicio: this.ejercicioVacio, tipo: '', cantidad: 0, peso: 0 });
      }
    },
    quitarRonda() {
      if (!this.soloLectura) {
        this.$emit('quitar-ronda', this.ronda.id);
      }
    },
    quitarSerie(id) {
      if (!this.soloLectura) {
        this.ronda.series = this.ronda.series.filter(serie => serie.id !== id);
      }
    },
    getItemKey(item) {
      return item.id;
    },
    actualizarSeries(nuevaSerie) {
      if (!this.soloLectura) {
        const index = this.ronda.series.findIndex(serie => serie.id === nuevaSerie.id);
        if (index !== -1) {
          this.ronda.series.splice(index, 1, nuevaSerie);
        }
      }
    }
  },
  watch: {
    ronda: {
      deep: true,
      handler(newRonda) {
        this.$emit('update-ronda', newRonda);
      }
    }
  },
  mounted() {
    if (this.soloLectura) {
    } else {
      this.ronda.tipo = 'REPS';
      this.agregarSerie();
    }
  }
};
</script>

<style scoped>
.serie::before {
  border-radius: 5px 0px 0px 5px;
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 5px;
  background-color: var(--claro);
}

.ronda-componente {
  padding: 20px;
}

.definicion-ronda {
  padding-left: 15px;
  font-size: 20px;
  margin-bottom: 16px;
}

.ronda-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.contenedor-input {
  display: flex;
  align-items: center;
}

.icono-papelera {
  cursor: pointer;
  margin-left: auto;
}

.lista-series {
  margin-top: 5px;
}

.input-corto {
  flex: 1;
}

.contenedor-flex {
  justify-content: space-between;
}

.flex-item {
  flex: 1;
}

.justify-start {
  display: flex;
  justify-content: flex-start;
}

.justify-end {
  display: flex;
  justify-content: flex-end;
}
</style>