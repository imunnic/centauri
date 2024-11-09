<!--
  Este componente `InputTiempoComponent` permite al usuario introducir y actualizar un tiempo en minutos y segundos. 
  Está construido con Vuetify (https://vuetifyjs.com/), usando el componente `v-text-field` para entradas de número y generando una interfaz intuitiva.

  Props:
  - `valorInicial`: Un valor numérico que representa el tiempo inicial en segundos. 
      - Tipo: `Number`
      - Opcional, por defecto: `0`
  - `titulo`: Una cadena de texto que se mostrará como título del componente.
      - Tipo: `String`
      - Opcional, sin valor predeterminado.

  Emits:
  - `nuevo-valor`: Este evento emite el tiempo total en segundos (calculado en `actualizarTiempo`)
    cada vez que el usuario actualiza los campos de minutos o segundos.

-->

<template>
  {{ titulo }}
  <div class="input-tiempo">
    <v-text-field
      class="input-corto"
      v-model="minutos"
      label="Minutos"
      type="number"
      min="0"
      @input="actualizarTiempo"
      placeholder="00"
    ></v-text-field>
    <span class="separador-tiempo">:</span>
    <v-text-field
      class="input-corto"
      v-model="segundos"
      label="Segundos"
      type="number"
      min="0"
      max="59"
      @input="actualizarTiempo"
      placeholder="00"
    ></v-text-field>
  </div>
</template>

<script>
export default {
  props: {
    valorInicial: {
      type: Number,
      required: false,
      default: 0
    },
    titulo:{
      type:String,
      required:false
    }
  },
  computed: {
    segundosFormateados: {
      get() {
        return this.formatearTiempo(this.segundos);
      },
      set(value) {
        let nuevoValor = parseInt(value, 10) || 0;
        if (nuevoValor >= 0 && nuevoValor <= 59) {
          this.segundos = nuevoValor;
        }
      }
    },
  },
  emits: ["nuevo-valor"],
  data() {
    return {
      minutos: 0,
      segundos: 0,
    };
  },
  methods: {
    actualizarTiempo() {
      let tiempoTotal = this.minutos * 60 + this.segundos * 1;
      this.$emit("nuevo-valor", tiempoTotal);
    },
  },
  mounted() {
    this.minutos = Math.floor(this.valorInicial / 60) || 0;
    this.segundos = this.valorInicial % 60;
    this.actualizarTiempo();
  },
};
</script>

<style scoped>
.input-tiempo {
  display: flex;
  align-items: center;
  width: auto;
}

.separador-tiempo {
  margin: 0 8px;
  font-size: 20px;
  font-weight: bold;
}

.input-corto {
  width: 100px;
}
</style>
