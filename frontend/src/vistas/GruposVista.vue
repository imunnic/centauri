<template>
  <MensajeAlertaComponent
    :mostrar="mostrarAlerta"
    :mensaje="mensajeAlerta"
    :tipo="tipoAlerta"
    @cerrar="mostrarAlerta = false"
  ></MensajeAlertaComponent>
  <v-container class="contenedor-flex">
    <ListaCrudComponent
      :busqueda="false"
      titulo="Solicitudes de acceso"
      :accionesPersonalizadas="accionesSolicitudes"
      :items="solicitudesAcceso"
      :cargando="cargando"
      :permisoEdicion="true"
      :permisoCreacion="false"
      @aceptar="validarSolicitud"
      @rechazar="negarSolicitud"
      class="solicitudes"
    >
    </ListaCrudComponent>
    <div class="miembros-grupo">
      <h1>Miembros:</h1>
      <div class="contenedor-flex selector">
        <v-select
          v-model="grupoSeleccionado"
          label="Grupo"
          :items="gruposEncargado"
          item-title="nombre"
          :return-object="true"
          @update:modelValue="cambioGrupoSeleccionado"
          class="selector-grupo"
        ></v-select>
      </div>
      <ListaCrudComponent
        :busqueda="false"
        :items="miembrosGrupoSeleccionado"
        :cargando="cargandoMiembros"
        :permisoEdicion="true"
        :permisoCreacion="false"
        :accionesPersonalizadas="accionesMiembros"
        @detalle="verMiembro"
        @cambiar-encargado="confirmarCambioEncargado"
      >
      </ListaCrudComponent>
      <DetalleUsuarioSesionesComponent
        v-if="mostrarDetalleUsuario"
        :sesionesRealizadas="sesionesRealizadasDeUsuario"
        @cerrar="cerrarMiembro"
      ></DetalleUsuarioSesionesComponent>
      <v-dialog v-model="mostrarConfirmacionCambioEncargado" max-width="600px">
        <v-card>
          <v-card-title class="flex-fila justify-space-between">
            <span>Confirmar cambio de encargado</span>
            <v-btn
              aria-label="cancelar-cambio-encargado"
              icon
              @click="cancelarCambioEncargado"
              flat
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text>
            ¿Está seguro de que desea cambiar el encargado? Perderá los
            privilegios sobre este grupo.
          </v-card-text>
          <div class="contenedor-flex botones">
            <v-btn
              aria-label="confirmar"
              class="claro boton"
              @click="aceptarCambioEncargado"
              >Confirmar</v-btn
            >
            <v-btn
              aria-label="cancelar-cambio-encargado"
              class="rechazo boton"
              @click="cancelarCambioEncargado"
              >Cancelar</v-btn
            >
          </div>
        </v-card>
      </v-dialog>
    </div>
  </v-container>
</template>
<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import DetalleUsuarioSesionesComponent from "@/components/DetalleUsuarioSesionesComponent.vue";
import MensajeAlertaComponent from "@/components/comun/MensajeAlertaComponent.vue";
import { useGruposStore } from "@/store/gruposStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { mapState, mapActions } from "pinia";
export default {
  components: {
    ListaCrudComponent,
    DetalleUsuarioSesionesComponent,
    MensajeAlertaComponent,
  },
  data() {
    return {
      accionesSolicitudes: [
        {
          icon: "mdi-thumb-up",
          color: "var(--claro)",
          evento: "aceptar",
        },
        {
          icon: "mdi-thumb-down",
          color: "var(--rechazo)",
          evento: "rechazar",
        },
      ],
      accionesMiembros: [
        {
          icon: "mdi-account-switch-outline",
          color: "var(--claro)",
          evento: "cambiar-encargado",
        },
      ],
      cargando: false,
      grupoSeleccionado: null,
      miembrosGrupoSeleccionado: [],
      cargandoMiembros: false,
      sesionesRealizadasDeUsuario: [],
      mostrarDetalleUsuario: false,
      mostrarConfirmacionCambioEncargado: false,
      miembroSeleccionado: null,
      mostrarAlerta: false,
      mensajeAlerta: "",
      tipoAlerta: "error",
    };
  },
  computed: {
    ...mapState(useGruposStore, [
      "gruposEncargado",
      "gruposRegistrados",
      "solicitudesAcceso",
    ]),
    ...mapState(useUsuariosStore, ["href"]),
  },
  methods: {
    ...mapActions(useGruposStore, [
      "getGruposEncargado",
      "getSolicitudes",
      "aceptarSolicitud",
      "rechazarSolicitud",
      "getMiembrosGrupo",
      "cambiarEncargado",
    ]),
    ...mapActions(useSesionesRealizadasStore, [
      "getSesionesRealizadasDeUsuarioyGrupo",
    ]),
    mostrarAlertaTemporal(mensaje, tipo) {
      this.mensajeAlerta = mensaje;
      this.tipoAlerta = tipo;
      this.mostrarAlerta = true;
    },
    async validarSolicitud(solicitud) {
      await this.aceptarSolicitud(solicitud._links.self.href);
      this.cargando = true;
      await this.getSolicitudes();
      this.cargando = false;
      try {
        await this.cambioGrupoSeleccionado(
          this.grupoSeleccionado._links.self.href
        );
      } catch {
        this.miembrosGrupoSeleccionado = [];
      }
    },
    async negarSolicitud(solicitud) {
      await this.rechazarSolicitud(solicitud._links.self.href);
      this.cargando = true;
      await this.getSolicitudes();
      this.cargando = false;
    },
    async cambioGrupoSeleccionado() {
      let grupoHref = this.grupoSeleccionado._links.self.href;
      this.cargandoMiembros = true;
      this.miembrosGrupoSeleccionado = await this.getMiembrosGrupo(grupoHref);
      this.cargandoMiembros = false;
    },
    confirmarCambioEncargado(encargado) {
      this.mostrarConfirmacionCambioEncargado = true;
      this.miembroSeleccionado = encargado;
    },
    cancelarCambioEncargado() {
      this.mostrarConfirmacionCambioEncargado = false;
      this.miembroSeleccionado = null;
    },
    async aceptarCambioEncargado() {
      let grupo = {
        encargado: this.miembroSeleccionado._links.self.href,
      };
      let grupoHref = this.grupoSeleccionado._links.self.href;
      try {
        await this.cambiarEncargado(grupoHref, grupo);
        this.mostrarAlertaTemporal("Encargado cambiado con éxito", "success");
      } catch {
        this.mostrarAlertaTemporal(
          "No se ha podido cambiar el encargado",
          "error"
        );
      }
      this.miembroSeleccionado = null;
      this.mostrarConfirmacionCambioEncargado = false;
      await this.cargarDatos();
    },
    async verMiembro(miembro) {
      let miembroId = miembro._links.self.href.split("/").pop();
      this.sesionesRealizadasDeUsuario =
        await this.getSesionesRealizadasDeUsuarioyGrupo(
          miembroId,
          this.grupoSeleccionado.nombre
        );
      this.mostrarDetalleUsuario = true;
    },
    cerrarMiembro() {
      this.mostrarDetalleUsuario = false;
      this.sesionesRealizadasDeUsuario = [];
    },
    async cargarDatos() {
      this.cargando = true;
      this.cargandoMiembros = true;
      await this.getGruposEncargado(this.href);
      await this.getSolicitudes();
      this.grupoSeleccionado = null;
      this.miembrosGrupoSeleccionado = [];
      this.cargando = false;
      this.cargandoMiembros = false;
    },
  },
  async mounted() {
    this.cargarDatos();
  },
};
</script>
<style scoped>
.contenedor-flex {
  align-items: start;
}

.selector {
  align-items: center !important;
  justify-content: space-between;
}

.miembros-grupo {
  width: 50%;
}

.solicitudes {
  width: 50%;
}

.selector-grupo {
  max-width: 25%;
  min-width: 200px;
}
.botones {
  justify-content: end;
  margin: 10px;
}
.boton {
  margin: 10px;
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

@media (max-width: 600px) {
  .contenedor-flex {
    flex-flow: column;
  }

  .solicitudes,
  .miembros-grupo {
    width: 100%;
    margin: 20px;
    padding: 0px;
  }
}
</style>
