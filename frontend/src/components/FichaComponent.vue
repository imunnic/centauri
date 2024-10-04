<template>
  <v-card class="ficha-componente contenedor">
    <v-row class="d-flex justify-space-between">
      <v-col cols="6">
        <div class="d-flex justify-start">
          <span class="info-titulo">Rondas:</span>
        </div>
      </v-col>
      <v-col cols="6" class="d-flex justify-end" v-if="!soloLectura">
        <v-btn
          aria-label="agregar-ronda"
          icon
          @click="agregarRonda"
          size="x-small"
          class="claro"
        >
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <draggable
      v-model="localRondas"
      class="lista-rondas"
      :item-key="getItemKey"
      :disabled="soloLectura || pantallaPequena"
    >
      <template #item="{ element, index }">
        <RondaComponent
          :key="element.id"
          :ronda="element"
          @quitar-ronda="quitarRonda"
          @update-ronda="actualizarRondas"
          :solo-lectura="soloLectura"
        />
      </template>
    </draggable>
  </v-card>
</template>

<script>
import RondaComponent from "./RondaComponent.vue";
import draggable from "vuedraggable";
import { v4 as uuidv4 } from "uuid";

export default {
  components: {
    RondaComponent,
    draggable,
  },
  props: {
    rondas: {
      type: Array,
      required: true,
    },
    soloLectura: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      localRondas: [...this.rondas],
      pantallaPequena: false,
    };
  },
  methods: {
    agregarRonda() {
      if (!this.soloLectura) {
        const nuevaRonda = {
          id: uuidv4(),
          tipo: "Reps",
          cantidad: 1,
          series: [],
        };
        this.localRondas.push(nuevaRonda);
        this.emitirActualizacion();
      }
    },
    quitarRonda(id) {
      if (!this.soloLectura) {
        this.localRondas = this.localRondas.filter((ronda) => ronda.id !== id);
        this.emitirActualizacion();
      }
    },
    getItemKey(item) {
      return item.id;
    },
    actualizarRondas(nuevaRonda) {
      if (!this.soloLectura) {
        const index = this.localRondas.findIndex(
          (ronda) => ronda.id === nuevaRonda.id
        );
        if (index !== -1) {
          this.localRondas.splice(index, 1, nuevaRonda);
          this.emitirActualizacion();
        }
      }
    },
    emitirActualizacion() {
      this.$emit("update-ficha", this.localRondas);
    },
    comprobarTamanoPantalla() {
      this.pantallaPequena = window.innerWidth < 1000;
    },
  },
  watch: {
    rondas: {
      deep: true,
      handler(newRondas) {
        this.localRondas = [...newRondas];
      },
    },
  },
  mounted() {
    this.comprobarTamanoPantalla(); 
    window.addEventListener("resize", this.comprobarTamanoPantalla);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.comprobarTamanoPantalla);
  },
};
</script>

<style scoped>
.ficha-componente {
  padding: 20px;
}

.lista-rondas {
  margin-top: 5px;
}
</style>
