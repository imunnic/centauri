<template>
  <div v-if="!inicio || sesionFin" class="contenedor-flex">
    <v-btn 
      v-if="!inicio"
      aria-label="iniciar-contador" 
      class="rechazo centrado boton" 
      :elevation="8"
      @click="iniciarContador">
      Inicio
    </v-btn>
    <v-btn 
      v-if="sesionFin"
      aria-label="finalizar-contador" 
      class="rechazo centrado boton fin" 
      :elevation="8"
      @click="finalizarContador">
      Finalizar
    </v-btn>
  </div>

  <div v-if="inicio" class="contador-tiempo-overlay">
    <span>{{ tiempoFormateado }}</span>
  </div>

  <div v-if="cargando" class="circulo-carga">
    <v-progress-circular
      :size="70"
      :width="7"
      indeterminate
      color="primary"
    ></v-progress-circular>
  </div>

  <div class="claro" v-if="!sesionFin && !cargando && sesion && inicio">
    <ContadorSesionComponent
      :sesion="sesion"
      :marcas="marcas"
      class="claro"
      @sesion-finalizada="sesionFin = true"
    />
  </div>

  <div class="claro" v-if="!sesionFin && !cargando && ficha && inicio">
    <ContadorFichaComponent
      :ficha="ficha"
      :marcas="marcas"
      class="claro"
      @ficha-finalizada="sesionFin = true"
    />
  </div>
</template>

<script>
import ContadorSesionComponent from "@/components/ContadorSesionComponent.vue";
import ContadorFichaComponent from "@/components/ContadorFichaComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useFichasStore } from "@/store/fichasStore";
import { mapActions } from "pinia";

export default {
  components: { ContadorSesionComponent, ContadorFichaComponent },
  data() {
    return {
      sesion: null,
      ficha: null,
      marcas: {
        "push press": 70,
        vam: 240,
        burpees: 50,
        "pull-ups": 30,
        "push-ups": 70,
        lunges: 100
      },
      sesionFin: false,
      cargando: false,
      inicio: false,
      tiempoSesion: 0,
      intervalo: null,
    };
  },
  computed: {
    tiempoFormateado() {
      const minutos = Math.floor(this.tiempoSesion / 60).toString().padStart(2, '0');
      const segundos = (this.tiempoSesion % 60).toString().padStart(2, '0');
      return `${minutos}:${segundos}`;
    }
  },
  methods: {
    ...mapActions(useSesionesStore, [
      "getFichasDeSesionConId",
      "getSesionPorId",
    ]),
    ...mapActions(useFichasStore, ["cargarFichaDetalle"]),
    
    iniciarContador() {
      this.inicio = true;
      this.tiempoSesion = 0;
      this.intervalo = setInterval(() => {
        this.tiempoSesion++;
      }, 1000);
    },
    
    finalizarContador() {
      clearInterval(this.intervalo);
      this.finalizar();
    },

    finalizar() {
      if (this.$route.query.sesion == "true") {
        this.$router.push("/usuario");
      } else {
        this.$router.push("/fichas");
      }
    }
  },
  async created() {
    if (this.$route.query.sesion == "true") {
      this.cargando = true;
      this.sesion = await this.getSesionPorId(this.$route.params.id);
      let fichas = await this.getFichasDeSesionConId(this.$route.params.id);
      this.sesion.fichas = fichas;
      this.cargando = false;
    } else {
      this.cargando = true;
      let response = await this.cargarFichaDetalle(this.$route.params.id);
      this.ficha = response.data;
      this.cargando = false;
    }
  },
  beforeDestroy() {
    clearInterval(this.intervalo);
  }
};
</script>

<style scoped>
.contenedor-flex {
  padding: 100px;
}
.boton {
  width: 200px;
  height: 60px;
  font-size: 30px;
}
.centrado {
  align-self: center;
  justify-self: center;
}
.claro:hover {
  background-color: var(--claro) !important;
}
.contador-tiempo-overlay {
  display: flex;
  justify-content: center;
  font-size: 50px;
  font-weight: bold;
  color: var(--texto);
  z-index: 1000;
}

</style>
