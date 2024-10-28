<template>
  <v-container>
    <div v-if="cargando" class="circulo-carga">
      <v-progress-circular
        :size="70"
        :width="7"
        indeterminate
        color="primary"
      ></v-progress-circular>
    </div>

    <div v-else id="elementoFicha">
      <h2>{{ fichaSeleccionada.nombre }}</h2>
      <div class="contenedor-flex">
        <v-chip class="claro" outlined>
          {{ fichaSeleccionada.tipoFicha }}
        </v-chip>
        <v-chip class="claro" outlined>
          {{ parteSesionFormateada }}
        </v-chip>
      </div>
      <div class="contenedor-flex">
        <v-text-field
          label="RPE Estimado"
          v-model="fichaSeleccionada.rpeEstimado"
          readonly
          class="input-corto"
          :class="colorRpe"
        ></v-text-field>
        <v-text-field
          label="Tiempo Estimado"
          v-model="tiempoEstimado"
          readonly
          class="input-medio"
        ></v-text-field>
        <v-text-field
          v-if="logeado"
          label="Autor"
          v-model="fichaSeleccionada.autor"
          readonly
          class="input-medio"
        ></v-text-field>
      </div>

      <b>Descripcion: </b> {{ fichaSeleccionada.descripcion }}

      <FichaComponent :rondas="fichaSeleccionada.rutina" :solo-lectura="true" />

      <FabBotonComponent
        :nombre="'aprobar-ficha'"
        v-if="perfil == 'ECEF' && fichaSeleccionada.estado == 'PENDIENTE'"
        :icon="'mdi-thumb-up-outline'"
        :clases-adicionales="'claro margen'"
        @click="dialogAprobar = true"
      >
      </FabBotonComponent>

      <FabBotonComponent
        :nombre="'rechazar-ficha'"
        v-if="perfil == 'ECEF' && fichaSeleccionada.estado == 'PENDIENTE'"
        :icon="'mdi-thumb-down-outline'"
        :clases-adicionales="'rechazo'"
        @click="dialogRechazar = true"
      >
      </FabBotonComponent>
      <FabBotonComponent
        nombre="ejecutar-ficha"
        v-if="fichaSeleccionada.estado == 'APROBADO'"
        icon="mdi-play-box-outline"
        @click="ejecutar"
      ></FabBotonComponent>
      <FabBotonComponent
        class="imprimir"
        nombre="imprimir-ficha"
        v-if="fichaSeleccionada.estado == 'APROBADO'"
        icon="mdi-printer-outline"
        @click="imprimirFicha"
      ></FabBotonComponent>
    </div>

    <v-dialog v-model="dialogAprobar" max-width="500px">
      <v-card>
        <v-card-title class="headline">Confirmar Acción</v-card-title>
        <v-card-text
          >¿Estás seguro de que quieres aprobar esta ficha?</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            aria-label="cancelar"
            class="rechazo"
            elevation="1"
            @click="dialogAprobar = false"
            >Cancelar</v-btn
          >
          <v-btn
            aria-label="aprobar"
            class="claro"
            elevation="1"
            @click="confirmar(true)"
            >Confirmar</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogRechazar" max-width="500px">
      <v-card>
        <v-card-title class="headline">Confirmar Acción</v-card-title>
        <v-card-text
          >¿Estás seguro de que quieres rechazar esta ficha?</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            name="cancelar"
            class="rechazo"
            elevation="1"
            @click="dialogRechazar = false"
            >Cancelar</v-btn
          >
          <v-btn
            name="rechazar"
            class="claro"
            elevation="1"
            @click="confirmar(false)"
            >Confirmar</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import FichaComponent from "@/components/FichaComponent.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";
import MensajeAlertaComponent from "@/components/comun/MensajeAlertaComponent.vue";
import { useFichasStore } from "../store/fichasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useImpresionStore } from "@/store/impresionStore";
import { mapState, mapActions } from "pinia";

export default {
  components: {
    FichaComponent,
    FabBotonComponent,
    MensajeAlertaComponent,
  },
  data() {
    return {
      fichaSeleccionada: null,
      cargando: false,
      dialogAprobar: false,
      dialogRechazar: false,
      tiempoEstimado: "",
      logeado: "false",
    };
  },
  computed: {
    ...mapState(useFichasStore, ["fichasRegistradas"]),
    ...mapState(useUsuariosStore, ["perfil", "token", "isLogged"]),
    colorRpe() {
      const rpe = this.fichaSeleccionada?.rpeEstimado || 0;
      if (rpe <= 5) {
        return "suave";
      } else if (rpe >= 6 && rpe <= 8) {
        return "umbral";
      } else if (rpe >= 9 && rpe <= 10) {
        return "muyAlto";
      }
      return "";
    },
    parteSesionFormateada() {
      const tipo = this.fichaSeleccionada.parteSesion;

      switch (tipo) {
        case "CALENTAMIENTO":
          return "Calentamiento";
        case "FUNDAMENTAL":
          return "Parte Fundamental";
        case "COORDINACION":
          return "Coordinación Funcional";
        case "CALMA":
          return "Vuelta a la calma";
        default:
          return tipo;
      }
    },
  },
  methods: {
    ...mapActions(useFichasStore, [
      "cargarFichaDetalle",
      "arrancarServicioFicha",
      "cambiarEstado",
    ]),
    ...mapActions(useUsuariosStore, ["peticionLogin", "getUsuario"]),
    ...mapActions(useImpresionStore, ["imprimir"]),

    async confirmar(aprobado) {
      this.dialogAprobar = false;
      await this.cambiarEstado(
        this.fichaSeleccionada._links.self.href,
        aprobado
      );
      let response = await this.cargarFichaDetalle(this.$route.params.id);
      this.fichaSeleccionada = response.data;
      this.$router.push("/fichas");
    },
    ejecutar() {
      this.$router.push("/contador/" + this.$route.params.id + "?sesion=false");
    },
    imprimirFicha() {
      this.imprimir("elementoFicha");
    },
  },
  async created() {
    this.cargando = true;
  },
  async mounted() {
    this.logeado = this.isLogged;
    let response = await this.cargarFichaDetalle(this.$route.params.id);
    this.fichaSeleccionada = response.data;
    this.cargando = false;
    this.tiempoEstimado = this.fichaSeleccionada.tiempoEstimado + " minutos";
    if (this.logeado) {
      this.fichaSeleccionada.autor = await this.getUsuario(
        this.fichaSeleccionada._links.autor.href
      );
    }
  },
};
</script>

<style scoped>
.margen {
  margin-right: 60px;
}

.contenedor-flex {
  gap: 10px;
  margin-bottom: 20px;
  justify-content: start !important;
}

.input-medio {
  max-width: 200px;
}

.circulo-carga {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.alert-container {
  position: fixed;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  width: 90%;
  max-width: 500px;
}
.suave {
  font-weight: 600;
  color: var(--suave);
}
.umbral {
  font-weight: 600;
  color: var(--elevado);
}
.muyAlto {
  font-weight: 600;
  color: var(--rechazo);
}

.imprimir{
  right: 100px;
}
</style>
