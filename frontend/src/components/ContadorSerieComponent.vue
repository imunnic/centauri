<template>
  <div class="contador-serie">
    <!-- Por tiempo -->
    <div v-if="tipo == 'TIEMPO'">
      <div v-if="serie.ajustable" class="claro">
        <v-progress-circular
        :model-value="progresoEnTiempo"
        size="250"
        width="10"
        class="progreso-circular claro"
        >
        <div class="contenido-circular">
          <p class="texto">{{ formatoTiempoRestante }}</p>
        </div>
      </v-progress-circular>
      <p class="texto nombre-ejercicio">{{ ejercicio.nombre }}</p>
      <p v-if="ejercicio.tipoCarga == 'VAM'" class="texto">
        Ritmo: {{ formatoVam }}
      </p>
      <p v-if="ejercicio.tipoCarga == 'RM'"class="texto">
        Carga: {{ marcaAjustada }} kg
      </p>
    </div>
    <div v-else>
      <v-progress-circular
      :model-value="progresoEnTiempo"
      size="200"
      width="10"
      class="progreso-circular claro"
      >
      <p class="texto">{{ formatoTiempoRestante }}</p>
    </v-progress-circular>
    <div class="contenido-circular">
      <p class="texto nombre-ejercicio">{{ ejercicio.nombre }}</p>
    </div>
    </div>
    </div>
    <!-- Si no son por tiempo -->
    <div v-else class="contenido">
      <p class="texto nombre-ejercicio">{{ ejercicio.nombre }}</p>
      <!-- ajustable -->
      <div v-if="serie.ajustable">
        <!-- serie de repeticiones -->
        <p
          class="texto cantidad-serie"
          v-if="tipo == 'REPS' && ejercicio.tipoCarga == 'REPS'"
        >
          {{ marcaAjustada }} repeticiones
        </p>
        <div
          class="cantidad-serie"
          v-if="tipo == 'REPS' && ejercicio.tipoCarga == 'RM'"
        >
          <p class="texto">{{ serie.cantidad }} repeticiones</p>
          <p class="texto">
            Carga: {{ marcaAjustada }} kg
          </p>
        </div>
        <!-- serie de distancia -->
        <div
          class="cantidad-serie"
          v-if="tipo == 'DIST' && ejercicio.tipoCarga == 'REPS'"
        >
          <p class="texto">{{ serie.cantidad }} m</p>
          <p class="texto">{{ marcaAjustada }} repeticioes</p>
        </div>
        <div
          class="cantidad-serie"
          v-if="tipo == 'DIST' && ejercicio.tipoCarga == 'VAM'"
        >
          <p class="texto">{{ serie.cantidad }} m</p>
          <p class="texto">
            Ritmo: {{ formatoVam }}
          </p>
        </div>
        <div
          class="cantidad-serie"
          v-if="tipo == 'DIST' && ejercicio.tipoCarga == 'RM'"
        >
          <p class="texto">{{ serie.cantidad }} m</p>
          <p class="texto">Carga: {{ marcaAjustada }} kg</p>
        </div class="texto">
      </div>
      <!-- no ajustable -->
      <div v-else>
        <!-- serie de repeticiones -->
        <p class="texto cantidad-serie" v-if="tipo == 'REPS'">
          {{ serie.cantidad }} repeticiones
        </p>
        <!-- serie de distancia -->
        <p class="texto cantidad-serie" v-if="tipo == 'DIST'">
          {{ serie.cantidad }} m
        </p>
      </div>
    </div>

    <v-card-actions>
      <v-btn class="rechazo boton" @click="finalizarSerie">{{ textoBoton }}</v-btn>
    </v-card-actions>
  </div>
</template>

<script>
export default {
  props: {
    serie: {
      type: Object,
      required: true,
    },
    marcasUsuario: {
      type: Object,
      required: true,
    },
    ultimo:{
      type: Boolean,
      required:true
    }
  },
  data() {
    return {
      tiempoRestante: this.serie.cantidad,
      intervalId: null,
    };
  },
  computed: {
    ejercicio() {
      return this.serie.ejercicio;
    },

    tipo() {
      return this.serie.tipo;
    },

    marcaAjustada() {
      let marcaUsuario =
        this.ejercicio.tipoCarga !== "VAM"
          ? this.marcasUsuario[this.ejercicio.nombre.toLowerCase()]
          : this.marcasUsuario.vam;
      if (this.serie.ajustable && this.ejercicio.tipoCarga !== 'VAM') {
        if (marcaUsuario) {
          const porcentaje = (this.serie.carga / 100) * marcaUsuario;
          return Math.round(porcentaje);
        } else {return 'Sin marca guardada'}
      } else if(this.serie.ajustable && this.ejercicio.tipoCarga == 'VAM') {
        if (marcaUsuario) {
          const porcentaje = (((100 - this.serie.carga) + 100) / 100) * marcaUsuario;
          return Math.round(porcentaje);
        } else {return 'Sin marca guardada'}
      } else {
        return this.serie.cantidad;
      }
    },

    formatoTiempoRestante() {
      return this.formatearTiempo(this.tiempoRestante);
    },

    formatoVam(){
      return this.formatearTiempo(this.marcaAjustada);
    },

    progresoEnTiempo() {
      return (this.tiempoRestante / this.serie.cantidad) * 100;
    },

    textoBoton(){
      if(this.ultimo){
        return 'Finalizar';
      } else {
        return 'Siguiente';
      }
    }
  },
  methods: {
    formatearTiempo(tiempoTotal) {
      const minutos = Math.floor(tiempoTotal / 60);
      const segundos = tiempoTotal % 60;
      return `${minutos.toString().padStart(2, "0")}:${segundos
        .toString()
        .padStart(2, "0")}`;
    },

    empezarTemporizador() {
      if (this.tipo === "TIEMPO") {
        this.intervalId = setInterval(() => {
          if (this.tiempoRestante > 0) {
            this.tiempoRestante--;
          } else {
            this.finalizarSerie();
          }
        }, 1000);
      }
    },

    finalizarSerie() {
      clearInterval(this.intervalId);
      this.$emit("serie-finalizada");
    },
  },
  mounted() {
    this.empezarTemporizador();
  },

  beforeDestroy() {
    clearInterval(this.intervalId);
  },
};
</script>

<style scoped>
.texto{
  font-size: 40px;
  text-align: center;
}
.contador-serie {
  display: flex;
  flex-flow: column;
  align-items: center;
  justify-content: center;
  width: fit-content;
  padding: 20px;
}

.progreso-circular {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.progreso-circular:hover {
  background-color: var(--claro);
}

.contenido {
  display: flex;
  flex-flow: column;
  align-items: center;
  justify-content: center;
  color: var(--texto);
}

.contenido-circular {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.nombre-ejercicio {
  font-weight: bold;
}

.tipo-serie {
  margin-top: 10px;
}

.cantidad-serie {
  margin-top: 10px;
}

.boton{
  width: fit-content;
  height: fit-content;
  border-radius: 20px;
  font-size: 30px;
  margin-top: 50px;
  padding: 10px;
}
</style>
