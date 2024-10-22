<template>
  <div class="contenedor-flex">
    <div class="barra-puntos">
      Rondas
      <div
        v-for="(punto, index) in puntos"
        :key="index"
        class="punto"
        :class="{ activo: index <= rondasActualTotal }"
      ></div>
    </div>

    <div class="contenido">
      <p class="texto">
        {{ textoSesion }}
      </p>
      <p class="texto nombre">
        {{ ficha.nombre }}
      </p>
      <v-divider :thickness="2" color="#ffffff" opacity="0.7"></v-divider>
      <ContadorRondaComponent class="claro" :key="rondaActual" :ronda="ronda" :marcasUsuario="marcas"
        :rondaTotal="rondas.length" @ronda-finalizada="siguienteRonda" @vuelta-completada="sumarRonda"/>
    </div>
  </div>
</template>


<script>
import ContadorRondaComponent from "@/components/ContadorRondaComponent.vue";
export default {
  props: {
    ficha: {
      type: Object,
      required: true
    },
    marcas: {
      type: Object,
      required: true
    }
  },
  emits: ['ficha-finalizada'],
  components: { ContadorRondaComponent },
  computed: {
    textoSesion() {
      let texto;
      switch (this.ficha.parteSesion) {
        case "CALENTAMIENTO":
          texto = "Calentamiento";
          break;
        case "FUNDAMENTAL":
          texto = "Parte fundamental";
          break;
        case "COORDINACION":
          texto = "Coordinación funcional";
          break;
        case "CALMA":
          texto = "Vuelta a la calma";
          break;
      }
      return texto;
    }
  },
  data() {
    return {
      rondas: [],
      ronda: {},
      rondaActual: 0,
      puntos: [],
      rondasActualTotal:0
    };
  },
  methods: {
    siguienteRonda() {
      this.rondaActual++;
      console.log(this.rondasActualTotal);
      this.ronda = this.rondas[this.rondaActual];
      if (this.rondaActual >= this.rondas.length) {
        this.$emit('ficha-finalizada');
      }
    },
    sumarRonda(){
      this.rondasActualTotal++;
    },
    calcularPuntos() {
      this.puntos = [];
      this.rondas.forEach(ronda => {
        if (ronda.tipo === 'REPS') {
          for (let i = 0; i < ronda.cantidad; i++) {
            this.puntos.push({});
          }
        } else if (ronda.tipo === 'TIEMPO') {
          this.puntos.push({});
        }
      });
    }
  },
  created() {
    this.rondas = this.ficha.rutina;
    this.ronda = this.rondas[0];
    this.calcularPuntos();
  }
};
</script>

<style scoped>
.texto{
  text-align: center;
  font-size: x-large;
}
.nombre{
  font-size: large;
}
.contenedor-flex {
  position: relative;
}

.barra-puntos {
  position: absolute;
  left: 20px;
  top: 100px;
  bottom: 0;
  width: fit-content;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow-y: auto;
  padding: 10px 0;
}

.punto {
  width: 10px;
  height: 10px;
  background-color: #ffffff;
  border-radius: 50%;
  margin: 5px 0;
  opacity: 0.5;
  transition: opacity 0.3s ease;
}

.punto.activo {
  opacity: 1;
}

.contenido {
  flex-grow: 1;
}
</style>
