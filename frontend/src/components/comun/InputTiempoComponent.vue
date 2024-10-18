<template>
  {{ titulo }}
  <div class="input-tiempo">
    <v-text-field
      v-model="minutos"
      label="Minutos"
      type="number"
      min="0"
      @input="actualizarTiempo"
      placeholder="00"
    ></v-text-field>
    <span class="separador-tiempo">:</span>
    <v-text-field
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
  width: 200px;
}

.separador-tiempo {
  margin: 0 8px;
  font-size: 20px;
  font-weight: bold;
}

.input-corto {
  width: 80px;
}
</style>
