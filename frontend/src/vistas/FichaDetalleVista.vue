<template>
  <v-container>
    <transition name="fade">
      <v-alert
        v-if="mostrarAlerta"
        :type="tipoAlerta"
        dismissible
        class="alert-container ma-2"
      >
        {{ mensajeAlerta }}
      </v-alert>
    </transition>

    <div v-if="cargando" class="circulo-carga">
      <v-progress-circular
        :size="70"
        :width="7"
        indeterminate
        color="primary"
      ></v-progress-circular>
    </div>

    <div v-else>
      <h2>Ver Ficha</h2>
      <div class="contenedor-flex">
        <v-text-field
          label="Nombre de la ficha"
          v-model="fichaSeleccionada.nombre"
          readonly
          class="input-field"
        ></v-text-field>
        <v-text-field
          label="Tipo de ficha"
          v-model="fichaSeleccionada.tipoFicha"
          readonly
          class="input-field"
        ></v-text-field>
        <v-text-field
          label="Parte del entrenamiento"
          v-model="fichaSeleccionada.parteSesion"
          readonly
          class="input-field"
        ></v-text-field>
      </div>

      <FichaComponent :rondas="fichaSeleccionada.rutina" :solo-lectura="true" />

      <FabBotonComponent
        v-if="perfil == 'ECEF' && fichaSeleccionada.estado == 'PENDIENTE'"
        :icon="'mdi-thumb-up-outline'"
        :clases-adicionales="'claro'"
        @click="dialogAprobar = true"
      >
      </FabBotonComponent>

      <FabBotonComponent
        v-if="perfil == 'ECEF' && fichaSeleccionada.estado == 'PENDIENTE'"
        :icon="'mdi-thumb-down-outline'"
        :clases-adicionales="'rechazo'"
        @click="dialogRechazar = true"
      >
      </FabBotonComponent>
    </div>

    <v-dialog v-model="dialogAprobar" max-width="500px">
      <v-card>
        <v-card-title class="headline">Confirmar Acción</v-card-title>
        <v-card-text
          >¿Estás seguro de que quieres aprobar esta ficha?</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialogAprobar = false"
            >Cancelar</v-btn
          >
          <v-btn color="green darken-1" text @click="confirmar(true)"
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
          <v-btn color="blue darken-1" text @click="dialogRechazar = false"
            >Cancelar</v-btn
          >
          <v-btn color="red darken-1" text @click="confirmar(false)"
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
import { useFichasStore } from "../store/fichasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapState, mapActions } from "pinia";

export default {
  components: {
    FichaComponent,
    FabBotonComponent,
  },
  data() {
    return {
      fichaSeleccionada: null,
      cargando: false,
      mostrarAlerta: false,
      mensajeAlerta: "",
      tipoAlerta: "",
      dialogAprobar: false,
      dialogRechazar: false,
    };
  },
  computed: {
    ...mapState(useFichasStore, ["fichasRegistradas"]),
    ...mapState(useUsuariosStore, ["perfil", "token", "isLogged"]),
  },
  methods: {
    ...mapActions(useFichasStore, [
      "cargarFichaDetalle",
      "arrancarServicioFicha",
      "cambiarEstado",
    ]),
    ...mapActions(useUsuariosStore, ["peticionLogin"]),

    async confirmar(aprobado) {
      this.dialogAprobar = false;
      await this.cambiarEstado(
        this.fichaSeleccionada._links.self.href,
        aprobado
      );
      let response = await this.cargarFichaDetalle(this.$route.params.id);
      this.fichaSeleccionada = response.data;
      if (aprobado) {
        this.mostrarAlertaTemporal("Ficha validada", "success");
      } else {
        this.mostrarAlertaTemporal("Ficha rechazada", "error");
      }
      this.$router.push("/fichas");
    },

    mostrarAlertaTemporal(mensaje, tipo = "success") {
      this.mensajeAlerta = mensaje;
      this.tipoAlerta = tipo;
      this.mostrarAlerta = true;

      setTimeout(() => {
        this.mostrarAlerta = false;
      }, 3000);
    },
  },
  async created() {
    this.cargando = true;
    if (!this.isLogged) {
      await this.peticionLogin();
      this.arrancarServicioFicha(this.token);
    }
  },
  async mounted() {
    let response = await this.cargarFichaDetalle(this.$route.params.id);
    this.fichaSeleccionada = response.data;
    this.cargando = false;
  },
};
</script>

<style scoped>
.claro {
  margin-right: 60px;
}

.contenedor-flex {
  gap: 10px;
  margin-bottom: 20px;
}

.input-field {
  max-width: 300px;
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
</style>
