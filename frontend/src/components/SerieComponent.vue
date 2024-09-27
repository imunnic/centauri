<template>
  <v-expansion-panels v-model="expansionPanelOpen" v-if="soloLectura == false">
    <v-expansion-panel :hide-actions="soloLectura" class="carta contenedor">
      <v-expansion-panel-title class="componente" style="position: relative;">
        <div class="contenedor">
          <div v-if="!ejercicioSeleccionadoNombre || ejercicioSeleccionadoNombre === 'Ejercicio no seleccionado'">
            <b>
              <p class="definicion-serie">
                Por favor, elija un ejercicio
              </p>
            </b>
          </div>
          <div v-else>
            <p class="definicion-serie" v-if="serie.tipo == 'REPS' && serie.ajustable">
              <b>{{ serie.carga }} % repeticiones de {{ ejercicioSeleccionadoNombre }}</b>
            </p>
            <p class="definicion-serie" v-if="serie.tipo == 'REPS' && !serie.ajustable">
              <b>{{ formatoDuracion }} repeticiones de {{ ejercicioSeleccionadoNombre }}</b>
            </p>
            <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
              <b>{{ formatoDuracion }} {{textoTiempo}} de {{ ejercicioSeleccionadoNombre }}</b>
            </p>
            <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
              <b>{{ formatoDuracion }} metros de {{ ejercicioSeleccionadoNombre }}</b>
            </p>
          </div>
          <div v-if="serie.ajustable && serie.tipo != 'REPS'">
            <p class="definicion-serie"><b>{{ serie.carga }} % de carga</b></p>
          </div>
          <div class="info"></div>
        </div>
      </v-expansion-panel-title>
      <v-expansion-panel-text v-if="soloLectura == false" style="position: relative;">
        <v-form class="contenedor">
          <v-select v-model="serie.ejercicio" label="Ejercicio" :items="ejerciciosRegistrados" item-title="nombre"
             item-value="_links.self.href" requiered :disabled="soloLectura"
            placeholder="Seleccione un ejercicio"></v-select>
          <div class="flex-container">
            <div class="input-medio">
              <v-select v-model="serie.tipo" :items="tiposSerie" item-title="texto" item-value="valor" label="Tipo" required
                :disabled="soloLectura"></v-select>
            </div>
            <div class="input-corto" v-if="!(serie.tipo == 'REPS' && serie.ajustable)">
              <v-text-field v-if="serie.tipo === 'REPS'" v-model="formatoDuracion" label="Repeticiones" type="number"
                required :disabled="soloLectura" hint="Cantidad" min="1"></v-text-field>
              <v-text-field v-if="serie.tipo === 'TIEMPO'" v-model="formatoTiempo" label="Tiempo" type="time" required
                :disabled="soloLectura" hint="Tiempo"></v-text-field>
              <v-text-field v-if="serie.tipo === 'DIST'" v-model="formatoDuracion" label="Distancia" type="number"
                required :disabled="soloLectura" hint="Distancia" min="1"></v-text-field>
            </div>
          </div>
          <v-text-field v-if="serie.ajustable" v-model="serie.carga" label="Carga (%)" type="number" required
            class="input-corto" :disabled="soloLectura" hint="Porcentaje de carga"></v-text-field>
          <v-checkbox v-model="serie.ajustable" label="Ajustable" :disabled="soloLectura"></v-checkbox>
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
          <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'"><b>{{ marcaEjercicio }} {{textoTiempo}}</b></p>
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
            <b>{{ formatoDuracion }} {{ textoTiempo }} a {{ marcaEjercicio }}</b>
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
            <b>{{ formatoDuracion }} {{textoTiempo}} con {{ marcaEjercicio }} kg</b>
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
        <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'"><b>{{ formatoDuracion }} {{ textoTiempo }}</b></p>
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
    },
    soloLectura: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      marcaObjetivo: 10,
      ritmoObjetivo: 240,
      expansionPanelOpen: [0],
      tiposSerie: [
        { valor: 'REPS', texto: 'Repeticiones' },
        { valor: 'TIEMPO', texto: 'Tiempo' },
        { valor: 'DIST', texto: 'Distancia' }
      ]
    };
  },
  computed: {
    ...mapState(useEjerciciosStore, ['ejerciciosRegistrados']),
    textoTiempo(){
      return this.serie.cantidad >= 60 ? 'minutos':'segundos'; 
    },
    ejercicioSeleccionadoNombre() {
      const ejercicioSeleccionado = this.ejerciciosRegistrados.find(
        ejercicio => ejercicio._links.self.href === this.serie.ejercicio
      );
      return ejercicioSeleccionado ? ejercicioSeleccionado.nombre : 'Ejercicio no seleccionado';
    },
    formatoMarca() {
      const minutos = Math.floor(this.ritmoObjetivo / 60).toString().padStart(2, '0');
      const seconds = (this.ritmoObjetivo % 60).toString().padStart(2, '0');
      return `${minutos}:${seconds}`;
    },

    formatoTiempo: {
    get() {
      const minutos = Math.floor(this.serie.cantidad / 60).toString().padStart(2, '0');
      const seconds = (this.serie.cantidad % 60).toString().padStart(2, '0');
      return `${minutos}:${seconds}`;
    },
    set(value) {
      const [minutos, seconds] = value.split(':').map(Number);
      this.serie.cantidad = (minutos * 60) + seconds;
    }
  },

    formatoDuracion: {
    get() {
      if (this.serie.tipo === 'TIEMPO') {
        return this.formatoTiempo;
      }
      return `${this.serie.cantidad}`;
    },
    set(value) {
      if (this.serie.tipo === 'TIEMPO') {
        const [minutos, seconds] = value.split(':').map(Number);
        this.serie.cantidad = (minutos * 60) + seconds;
      } else {
        this.serie.cantidad = Number(value);
      }
    }
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

  watch: {
    'serie.ejercicio': function(newEjercicioHref) {
      const ejercicio = this.ejerciciosRegistrados.find(ej => ej._links.self.href === newEjercicioHref);
      if (ejercicio) {
        this.serie.ejercicio = ejercicio;
      }
    }
  },

  methods: {
    onInput(value) {
      const [minutos, seconds] = value.split(':').map(Number);
      if (!isNaN(minutos) && !isNaN(seconds)) {
        this.marcaObjetivo = (minutos * 60) + seconds;
      }
    },
    quitarSerie() {
      this.$emit('quitar-serie', this.serie.id);
    }
  },
  created() {
    if (!this.soloLectura) {
      this.serie.tipo = 'REPS';
      this.serie.cantidad = this.serie.cantidad || 10;
      this.serie.carga = this.serie.carga || 50;
      this.serie.ajustable = false;
    }
  }
};
</script>


<style scoped>
.carta {
  margin-bottom: 10px;
}

.contenedor {
  margin-bottom: 10px;
}

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
  min-width: 150px;
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