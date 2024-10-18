<template>
  <v-expansion-panels v-model="expansionPanelOpen" v-if="soloLectura == false">
    <v-expansion-panel :hide-actions="soloLectura" class="carta contenedor">
      <v-expansion-panel-title class="componente" style="position: relative">
        <div class="contenedor">
          <div
            v-if="
              !ejercicioSeleccionadoNombre ||
              ejercicioSeleccionadoNombre === 'Ejercicio no seleccionado'
            "
          >
            <b>
              <p class="definicion-serie">Por favor, elija un ejercicio</p>
            </b>
          </div>
          <div v-else>
            <p
              class="definicion-serie"
              v-if="serie.tipo == 'REPS' && serie.ajustable"
            >
              <b
                >{{ serie.carga }} % repeticiones de
                {{ ejercicioSeleccionadoNombre }}</b
              >
            </p>
            <p
              class="definicion-serie"
              v-if="serie.tipo == 'REPS' && !serie.ajustable"
            >
              <b
                >{{ formatoDuracion }} repeticiones de
                {{ ejercicioSeleccionadoNombre }}</b
              >
            </p>
            <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
              <b
                >{{ formatoDuracion }} {{ textoTiempo }} de
                {{ ejercicioSeleccionadoNombre }}</b
              >
            </p>
            <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
              <b
                >{{ formatoDuracion }} metros de
                {{ ejercicioSeleccionadoNombre }}</b
              >
            </p>
          </div>
          <div v-if="serie.ajustable && serie.tipo != 'REPS'">
            <p class="definicion-serie">
              <b>{{ serie.carga }} % de carga</b>
            </p>
          </div>
          <div class="info"></div>
        </div>
      </v-expansion-panel-title>
      <v-expansion-panel-text
        v-if="soloLectura == false"
        style="position: relative"
      >
        <v-form class="contenedor">
          <v-combobox
            v-model="serie.ejercicio"
            label="Ejercicio"
            :items="ejerciciosRegistrados"
            item-title="nombre"
            item-value="_links.self.href"
            :return-object="false"
            :filter="filtroEjercicios"
            :rules="[isSeleccionValida]"
            requiered
            :disabled="soloLectura"
            class="placeholder"
            placeholder="Extensiones"
            clearable
            solo
          ></v-combobox>
          <div class="flex-container">
            <div class="input-medio">
              <v-select
                v-model="serie.tipo"
                :items="tiposSerie"
                item-title="texto"
                item-value="valor"
                label="Tipo"
                required
                class="placeholder"
                placeholder="Repeticiones"
                :disabled="soloLectura"
              ></v-select>
            </div>
            <div
              class="input-corto"
              v-if="!(serie.tipo == 'REPS' && serie.ajustable)"
            >
              <v-text-field
                v-if="serie.tipo === 'REPS'"
                v-model="formatoDuracion"
                label="Repeticiones"
                type="number"
                required
                :disabled="soloLectura"
                class="placeholder"
                placeholder="10"
                hint="Cantidad"
                min="1"
              ></v-text-field>
              <InputTiempoComponent
                v-if="serie.tipo === 'TIEMPO'"
                :titulo="'Tiempo'"
                @nuevo-valor="actualizarTiempo"
              ></InputTiempoComponent>
              <v-text-field
                v-if="serie.tipo === 'DIST'"
                v-model="formatoDuracion"
                label="Distancia"
                type="number"
                required
                class="placeholder"
                placeholder="100"
                :disabled="soloLectura"
                hint="Distancia"
                min="1"
              ></v-text-field>
            </div>
          </div>
          <v-text-field
            v-if="serie.ajustable"
            v-model="serie.carga"
            label="Carga (%)"
            type="number"
            required
            class="input-corto placeholder"
            placeholder="50"
            :disabled="soloLectura"
            hint="Porcentaje de carga"
          ></v-text-field>
          <v-checkbox
            v-model="serie.ajustable"
            label="Ajustable"
            :disabled="soloLectura"
          ></v-checkbox>
        </v-form>
      </v-expansion-panel-text>
    </v-expansion-panel>
    <v-icon
      color="error"
      @click.stop="quitarSerie"
      style="position: absolute; top: 0; right: 0; margin: 8px"
      v-if="!soloLectura"
    >
      mdi-trash-can
    </v-icon>
  </v-expansion-panels>
  <v-card v-else class="carta" elevation="3">
    <div class="contenedor-flex flex-columna">
      <v-card-title class="titulo-serie"
        >Serie de {{ serie.ejercicio.nombre }}</v-card-title
      >
      <div
        v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'REPS'"
        class="flex-fila"
      >
        <div class="info-serie">
          <p class="definicion-serie">
            <b>Carga: {{ serie.carga }} %</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'REPS'">
            <b>{{ marcaEjercicio }} repeticiones</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
            <b>{{ marcaEjercicio }} {{ textoTiempo }}</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
            <b>{{ marcaEjercicio }} metros</b>
          </p>
        </div>
        <div class="info-serie">
          <div class="flex-item">
            <InformacionComponent
              :texto="ayudaMarca"
              :etiqueta="'Marca'"
            ></InformacionComponent>
            <v-text-field
              v-model.number="marcaObjetivo"
              :label="'Marca'"
              type="number"
              min="0"
              step="1"
              class="input-corto placeholder"
              placeholder="10"
            ></v-text-field>
          </div>
        </div>
      </div>
      <div
        v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'VAM'"
        class="flex-fila"
      >
        <div class="info-serie">
          <p class="definicion-serie">
            <b>Carga: {{ serie.carga }} %</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
            <b
              >{{ formatoDuracion }} {{ textoTiempo }} a {{ marcaEjercicio }}</b
            >
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
            <b>{{ formatoDuracion }} metros a {{ marcaEjercicio }}</b>
          </p>
        </div>
        <div class="info-serie">
          <div class="flex-item">
            <InformacionComponent
              :texto="ayudaMarca"
              :etiqueta="'Marca'"
            ></InformacionComponent>
            <InputTiempoComponent
              :valorInicial="240"
              @nuevo-valor="actualizarMarca"
            ></InputTiempoComponent>
          </div>
        </div>
      </div>
      <div
        v-if="serie.ajustable && serie.ejercicio.tipoCarga == 'RM'"
        class="flex-fila"
      >
        <div class="info-serie">
          <p class="definicion-serie">
            <b>Carga: {{ serie.carga }} %</b>
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'REPS'">
            <b
              >{{ formatoDuracion }} repeticiones con {{ marcaEjercicio }} kg</b
            >
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
            <b
              >{{ formatoDuracion }} {{ textoTiempo }} con
              {{ marcaEjercicio }} kg</b
            >
          </p>
          <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
            <b>{{ formatoDuracion }} metros con {{ marcaEjercicio }} kg</b>
          </p>
        </div>
        <div class="info-serie">
          <div class="flex-item">
            <InformacionComponent
              :texto="ayudaMarca"
              :etiqueta="'Marca'"
            ></InformacionComponent>
            <v-text-field
              v-model.number="marcaObjetivo"
              :label="'Marca'"
              type="number"
              min="0"
              step="1"
              class="input-corto placeholder"
              placeholder="50"
            ></v-text-field>
          </div>
        </div>
      </div>
      <div v-if="serie.ajustable == false">
        <p class="definicion-serie" v-if="serie.tipo == 'REPS'">
          <b>{{ formatoDuracion }} repeticiones</b>
        </p>
        <p class="definicion-serie" v-if="serie.tipo == 'TIEMPO'">
          <b>{{ formatoDuracion }} {{ textoTiempo }}</b>
        </p>
        <p class="definicion-serie" v-if="serie.tipo == 'DIST'">
          <b>{{ formatoDuracion }} metros</b>
        </p>
      </div>
    </div>
  </v-card>
</template>

<script>
import InformacionComponent from "@/components/comun/InformacionComponent.vue";
import InputTiempoComponent from "@/components/comun/InputTiempoComponent.vue";
import { useEjerciciosStore } from "../store/ejerciciosStore.js";
import { mapState } from "pinia";
import tooltips from "@/tooltips.json";

export default {
  components: { InformacionComponent, InputTiempoComponent },
  props: {
    serie: {
      type: Object,
    },
    soloLectura: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      marcaObjetivo: 10,
      ritmoObjetivo: 240,
      expansionPanelOpen: [0],
      tiposSerie: [
        { valor: "REPS", texto: "Repeticiones" },
        { valor: "TIEMPO", texto: "Tiempo" },
        { valor: "DIST", texto: "Distancia" },
      ],
      ayudaMarca: tooltips.ayudaMarca,
    };
  },
  computed: {
    ...mapState(useEjerciciosStore, ["ejerciciosRegistrados"]),
    textoTiempo() {
      return this.serie.cantidad >= 60 ? "minutos" : "segundos";
    },
    ejercicioSeleccionadoNombre() {
      const ejercicioSeleccionado = this.serie.ejercicio;
      return ejercicioSeleccionado
        ? ejercicioSeleccionado.nombre
        : "Ejercicio no seleccionado";
    },

    formatoTiempo: {
      get() {
        const minutos = Math.floor(this.serie.cantidad / 60)
          .toString()
          .padStart(2, "0");
        const seconds = (this.serie.cantidad % 60).toString().padStart(2, "0");
        return `${minutos}:${seconds}`;
      },
      set(value) {
        const [minutos, seconds] = value.split(":").map(Number);
        this.serie.cantidad = minutos * 60 + seconds;
      },
    },

    formatoDuracion: {
      get() {
        if (this.serie.tipo === "TIEMPO") {
          return this.formatoTiempo;
        }
        return `${this.serie.cantidad}`;
      },
      set(value) {
        if (this.serie.tipo === "TIEMPO") {
          const [minutos, seconds] = value.split(":").map(Number);
          this.serie.cantidad = minutos * 60 + seconds;
        } else {
          this.serie.cantidad = Number(value);
        }
      },
    },

    marcaEjercicio() {
      if (this.serie.ejercicio.tipoCarga === "VAM") {
        const ritmoEnSegundos =
          this.ritmoObjetivo +
          this.ritmoObjetivo * ((100 - this.serie.carga) / 100);
        const minutos = Math.floor(ritmoEnSegundos / 60)
          .toString()
          .padStart(2, "0");
        const segundos = (ritmoEnSegundos % 60)
          .toFixed(0)
          .toString()
          .padStart(2, "0");
        return `${minutos}:${segundos} min/km`;
      } else {
        return (this.serie.carga * this.marcaObjetivo * 0.01).toFixed(0);
      }
    },
  },

  watch: {
    "serie.ejercicio": function (newEjercicioHref) {
      const ejercicio = this.ejerciciosRegistrados.find(
        (ej) => ej._links.self.href === newEjercicioHref
      );
      if (ejercicio) {
        this.serie.ejercicio = ejercicio;
      }
    },
  },

  methods: {
    actualizarTiempo(nuevoValor) {
      this.serie.cantidad = nuevoValor;
    },

    actualizarMarca(nuevoValor) {
      this.ritmoObjetivo = nuevoValor;
    },

    quitarSerie() {
      this.$emit("quitar-serie", this.serie.id);
    },

    filtroEjercicios(item, queryText) {
      const nombre = item.nombre.toLowerCase();
      const query = queryText.toLowerCase();
      return nombre.indexOf(query) > -1;
    },

    isSeleccionValida(valor) {
      let isValido = false;
      if (valor) {
        if (valor.nombre) {
          isValido = this.ejerciciosRegistrados.some(
            (ejercicio) => ejercicio.nombre === valor.nombre
          );
        }
      }
      return isValido ? true : "Ejercicio no valido";
    },
  },
  created() {
    if (!this.soloLectura) {
      this.serie.tipo = "REPS";
      this.serie.cantidad = this.serie.cantidad || 10;
      this.serie.carga = this.serie.carga || 50;
      this.serie.ajustable = false;
    }
  },
};
</script>

<style scoped>
.carta {
  margin-bottom: 10px;
}

.titulo-serie {
  font-size: 20px;
  font-weight: 600;
  text-decoration: underline;
  word-wrap: break-word;
  overflow-wrap: break-word;
  white-space: normal;
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

  .flex-fila{
    flex-flow: column !important;
  }

  .flex-item {
    min-width: unset;
  }
}
</style>
