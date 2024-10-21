<template>
  <div class="ronda-contenedor">
    <div v-if="!rondaFinalizada">
      <h2 class="texto">
        Serie {{ indiceSerieActual + 1 }} de {{ ronda.series.length }}
      </h2>
      <ContadorSerieComponent
        v-if="indiceSerieActual < ronda.series.length"
        :serie="serieActual"
        :marcasUsuario="marcas"
        :key="serieKey"
        @serie-finalizada="finalizarSerie"
      />
      <div
        v-if="ronda.tipo === 'TIEMPO' && tiempoRestante > 0"
        class="texto tiempo-ronda"
      >
        <p>Tiempo de ronda:</p> 
        {{ formatearTiempo(tiempoRestante) }}
      </div>
    </div>
  </div>
</template>

<script>
import ContadorSerieComponent from "@/components/ContadorSerieComponent.vue";

export default {
  components: { ContadorSerieComponent },
  props: {
    ronda: {
      type: Object,
      required: true,
    },
    marcasUsuario: {
      type: Object,
      required: true,
    },
    rondaTotal: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      indiceSerieActual: 0,
      indiceSerieActual: 0,
      repeticionesRonda: 1,
      tiempoRestante: this.ronda.tipo === "TIEMPO" ? this.ronda.cantidad : 0,
      intervalId: null,
      serieKey: 0,
      marcas: this.marcasUsuario,
    };
  },
  computed: {
    serieActual() {
      return this.ronda.series[this.indiceSerieActual];
    },

    rondaFinalizada() {
      return (
        (this.ronda.tipo === "REPS" &&
          this.repeticionesRonda > this.ronda.cantidad) ||
        (this.ronda.tipo === "TIEMPO" && this.tiempoRestante <= 0)
      );
    },
  },
  methods: {
    formatearTiempo(tiempoTotal) {
      const minutos = Math.floor(tiempoTotal / 60);
      const segundos = tiempoTotal % 60;
      return `${minutos.toString().padStart(2, "0")}:${segundos
        .toString()
        .padStart(2, "0")}`;
    },

    iniciarRonda() {
      this.indiceSerieActual = 0;
      this.repeticionesRonda = 1;
      if (this.ronda.tipo === "TIEMPO") {
        this.tiempoRestante = this.ronda.cantidad;
        this.empezarTemporizador();
      }
    },

    finalizarSerie() {
      if (this.indiceSerieActual < this.ronda.series.length - 1) {
        this.indiceSerieActual++;
        
      } else if (this.ronda.tipo === "REPS") {
        this.indiceSerieActual = 0;
        this.repeticionesRonda++;
      } else if (this.ronda.tipo === "TIEMPO"){
        if(this.indiceSerieActual < this.ronda.series.length - 1){
          this.indiceSerieActual++;
        } else {
          this.indiceSerieActual = 0;
        }
      }

      if (this.rondaFinalizada) {
        clearInterval(this.intervalId);
        this.emitirRondaFinalizada();
      }
      this.serieKey++;
    },

    empezarTemporizador() {
      this.intervalId = setInterval(() => {
        if (this.tiempoRestante > 0) {
          this.tiempoRestante--;
        } else {
          clearInterval(this.intervalId);
          this.emitirRondaFinalizada();
        }
      }, 1000);
    },

    emitirRondaFinalizada() {
      clearInterval(this.intervalId);
      this.$emit("ronda-finalizada");
    },
  },
  mounted() {
    if (this.ronda.tipo === "TIEMPO") {
      this.empezarTemporizador();
    }
  },
  beforeDestroy() {
    clearInterval(this.intervalId);
  },
};
</script>

<style scoped>
.texto {
  font-size: 24px;
  text-align: center;
}
.ronda-contenedor {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.boton {
  margin-top: 20px;
  font-size: 18px;
}
.tiempo-ronda {
  margin-top: 10px;
  font-size: 30px;
}
</style>
