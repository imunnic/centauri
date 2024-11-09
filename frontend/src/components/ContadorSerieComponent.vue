<template>
  <div class="contador-serie">
    <!-- Por tiempo -->
    <div v-if="tipo == 'TIEMPO'">
      <div v-if="serie.ajustable" class="claro temporizador minAltura">
        <v-progress-circular
        :model-value="progresoEnTiempo"
        size="250"
        width="10"
        class="progreso-circular claro"
        >
          <p class="texto">{{ formatoTiempoRestante }}</p>
      </v-progress-circular>
      
      <a class="texto nombre-ejercicio" @click="mostrarEjercicio">{{ ejercicio.nombre }}</a>
      <p v-if="ejercicio.tipoCarga == 'VAM' || ejercicio.tipoCarga == 'TIEMPO'" 
      class="texto">
        Ritmo: {{ formatoVam }}
      </p>
      <p v-if="ejercicio.tipoCarga == 'RM'"class="texto">
        Carga: {{ marcaAjustada }} kg
      </p>
    </div>
    <div v-else class="contador-serie minAltura">
      <v-progress-circular
      :model-value="progresoEnTiempo"
      size="200"
      width="10"
      class="progreso-circular claro"
      >
      <p class="texto">{{ formatoTiempoRestante }}</p>
    </v-progress-circular>

      <a class="texto nombre-ejercicio" @click="mostrarEjercicio">{{ ejercicio.nombre }}</a>
    </div>
    </div>
    <!-- Si no son por tiempo -->
    <div v-else class="contenido minAltura">
      <a class="texto nombre-ejercicio" @click="mostrarEjercicio">{{ ejercicio.nombre }}</a>
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
          v-if="tipo == 'DIST' && 
          (ejercicio.tipoCarga == 'VAM' || ejercicio.tipoCarga == 'TIEMPO')"
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
      <v-btn 
      aria-label="iniciar-sesion" 
      class="rechazo boton" 
      @click="finalizarSerie">
        Siguiente Serie
      </v-btn>
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
      if(this.marcasUsuario != {}){
        if(this.ejercicio.tipoCarga == 'VAM' || this.ejercicio.tipoCarga == 'TIEMPO'){
          return 280;
        } else {
          return 10;
        }
      }
      let marcaUsuario = this.marcasUsuario[this.ejercicio.nombre];
      if (this.serie.ajustable && 
        (this.ejercicio.tipoCarga !== 'VAM' || this.ejercicio.tipoCarga !== 'TIEMPO')) {
        if (marcaUsuario) {
          const porcentaje = (this.serie.carga / 100) * marcaUsuario;
          return Math.round(porcentaje);
        } else {
          return 'Sin marca guardada';
        }
      } else if(this.serie.ajustable && 
        (this.ejercicio.tipoCarga == 'VAM'||this.ejercicio.tipoCarga == 'TIEMPO')) {
        if (marcaUsuario) {
          const porcentaje = (((100 - this.serie.carga) + 100) / 100) * marcaUsuario;
          return Math.round(porcentaje);
        } else {
          return 'Sin marca guardada';
        }
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
  },
  methods: {
    formatearTiempo(tiempoTotal) {
      let minutos = Math.floor(tiempoTotal / 60);
      let segundos = tiempoTotal % 60;
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
      window.speechSynthesis.cancel()
      this.$emit("serie-finalizada");
    },

    leerEnVozAlta(texto) {
      const utterance = new SpeechSynthesisUtterance(texto);

      utterance.lang = 'es-ES';
      utterance.rate =0.8;
      utterance.pitch = 0.8;
      utterance.volume = 1.2;

      const voices = window.speechSynthesis.getVoices();
      utterance.voice = voices.filter(voice => voice.name.includes('Spanish'))[42];

      window.speechSynthesis.speak(utterance);
    },

    mostrarEjercicio(){
      this.$emit("mostrar-ejercicio", this.serie.ejercicio);
    }
  },
  mounted() {
    this.empezarTemporizador();
    this.leerEnVozAlta(this.ejercicio.nombre);
    if(this.tipo == 'TIEMPO'){
      let minutos = Math.floor(this.serie.cantidad / 60);
      let segundos = this.serie.cantidad % 60;
      this.leerEnVozAlta(minutos + ' minutos, ' + segundos + ' segundos');
    } else if(this.tipo == 'REPS'){
      this.leerEnVozAlta(this.marcaAjustada + ' repeticiones')
    } else {
      this.leerEnVozAlta(this.marcaAjustada + ' metros')
    }
  },

  beforeDestroy() {
    clearInterval(this.intervalId);
  },
};
</script>

<style scoped>
.minAltura{
  min-height: 300px;
}
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
  padding: 10px;
}
.temporizador{
  display: flex;
  flex-flow: column;
  justify-content: center;
  align-items: center;
}
.progreso-circular {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.contador-serie .progreso-circular {
    background-color: transparent !important;
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
  margin-top: 20px;
  padding: 10px;
}

a:hover{
  cursor: pointer;
}
</style>
