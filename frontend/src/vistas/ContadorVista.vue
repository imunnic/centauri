<template>
  <div v-if="!inicio || sesionFin" class="contenedor-flex padding">
    <v-btn
      v-if="!inicio"
      aria-label="iniciar-contador"
      class="rechazo centrado boton"
      :elevation="8"
      @click="iniciarContador"
    >
      Inicio
    </v-btn>
    <v-btn
      v-if="sesionFin"
      aria-label="finalizar-contador"
      class="rechazo centrado boton fin"
      :elevation="8"
      @click="finSesion"
    >
      Finalizar
    </v-btn>
  </div>
  <v-dialog v-model="formFinalizarSesion" persistent max-width="600px">
    <v-card v-if="!sesionRealizada">
      <v-card-title class="d-flex justify-space-between align-center">
        <span>Valorar Sesión</span>
        <v-btn
          aria-label="cerrar"
          icon
          @click="formFinalizarSesion = false"
          flat
        >
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <SesionRealizadaComponent

        :tiempoFinal="minutosTotales"
        @sesion-realizada="realizarSesion"
        @salir="finalizarContador"
      ></SesionRealizadaComponent>
    </v-card>
  </v-dialog>

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
      @mostrar-ejercicio="mostrarEjercicio"
    />
  </div>

  <div class="claro" v-if="!sesionFin && !cargando && ficha && inicio">
    <ContadorFichaComponent
      :ficha="ficha"
      :marcas="marcas"
      class="claro"
      @ficha-finalizada="sesionFin = true"
      @mostrar-ejercicio="mostrarEjercicio"
    />
  </div>

  <EjercicioDetalleComponent
      v-if="detalleEjercicio"
      :ejercicio="ejercicio"
      @cerrar="cerrarDialogoDetalle"
    />
</template>

<script>
import ContadorSesionComponent from "@/components/ContadorSesionComponent.vue";
import ContadorFichaComponent from "@/components/ContadorFichaComponent.vue";
import SesionRealizadaComponent from "@/components/SesionRealizadaComponent.vue";
import EjercicioDetalleComponent from "@/components/EjercicioDetalleComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useFichasStore } from "@/store/fichasStore";
import { mapActions, mapState } from "pinia";

export default {
  components: {
    ContadorSesionComponent,
    ContadorFichaComponent,
    SesionRealizadaComponent,
    EjercicioDetalleComponent
  },
  data() {
    return {
      sesion: null,
      ficha: null,
      sesionFin: false,
      cargando: false,
      inicio: false,
      tiempoSesion: 0,
      intervalo: null,
      formFinalizarSesion: false,
      detalleEjercicio: false,
      ejercicio: null,
      sesionRealizada:false
    };
  },
  computed: {
    ...mapState(useUsuariosStore,['id','marcas','href']),
    tiempoFormateado() {
      const minutos = Math.floor(this.tiempoSesion / 60)
        .toString()
        .padStart(2, "0");
      const segundos = (this.tiempoSesion % 60).toString().padStart(2, "0");
      return `${minutos}:${segundos}`;
    },
    minutosTotales() {
      return Math.floor(this.tiempoSesion / 60);
    },
  },
  methods: {
    ...mapActions(useSesionesStore, [
      "getFichasDeSesionConId",
      "getSesionPorId",
    ]),
    ...mapActions(useFichasStore, ["cargarFichaDetalle"]),
    ...mapActions(useSesionesRealizadasStore, [
      "crearSesionRealizada", "comprobarSesionRealizada"
    ]),
    iniciarContador() {
      this.inicio = true;
      this.tiempoSesion = 0;
      this.intervalo = setInterval(() => {
        this.tiempoSesion++;
      }, 1000);
    },

    finSesion(){
      if (this.$route.query.sesion == "true" && !this.sesionRealizada){
          this.formFinalizarSesion = true;
      } else {
        this.finalizarContador();
      }
    },

    async realizarSesion(sesionRealizada) {
      sesionRealizada.sesion = {
        id: this.$route.params.id,
      };
      sesionRealizada.usuario = { id: this.id };
      await this.crearSesionRealizada(sesionRealizada);
      this.finalizarContador();
    },

    finalizarContador() {
      clearInterval(this.intervalo);
      this.finalizar();
    },

    finalizar() {
      if (this.$route.query.sesion == "true") {
        this.$router.push("/planificacion");
      } else {
        this.$router.push("/fichas");
      }
    },

    mostrarEjercicio(ejercicio){
      this.ejercicio = ejercicio;
      this.detalleEjercicio = true;
    },

    cerrarDialogoDetalle(){
      this.ejercicio = null;
      this.detalleEjercicio = false;
    }
  },
  async created() {
    if (this.$route.query.sesion == "true") {
      this.cargando = true;
      try {
        this.sesion = await this.getSesionPorId(this.$route.params.id);
      } catch (error) {
        this.finalizar();
      }

      this.sesionRealizada = await this.comprobarSesionRealizada(this.href, this.sesion._links.self.href);
      let fichas = await this.getFichasDeSesionConId(this.$route.params.id);
      this.sesion.fichas = fichas;
      this.cargando = false;
    } else {
      this.cargando = true;
      try {
        let response = await this.cargarFichaDetalle(this.$route.params.id);
        this.ficha = response.data;
      } catch (error) {
        this.finalizar();
      }
      this.cargando = false;
    }
  },
  beforeDestroy() {
    clearInterval(this.intervalo);
  },
};
</script>

<style scoped>
.padding {
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
