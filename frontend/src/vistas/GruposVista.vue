<template>
  <v-container class="contenedor-flex columna">
    <v-card class="elevacion solicitudes">
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
      >
        <template v-slot:titulo="{ item }">
          Solicitud de
          <v-chip class="claro">{{ item.nombreUsuario }}</v-chip> para
          <v-chip class="claro">{{ item.nombreGrupo }}</v-chip>
        </template>
      </ListaCrudComponent>
    </v-card>
    <v-card class="elevacion">
      <h2>Miembros:</h2>
      <div class="contenedor-flex selector">
        <v-combobox
          v-model="grupoSeleccionado"
          label="Grupo"
          :items="gruposEncargado"
          item-title="nombre"
          :return-object="true"
          :filter="filtroGrupos"
          :rules="[isSeleccionValida]"
          append-icon="mdi-trash-can"
          @click:append-inner="borradoDeGrupo"
          @update:modelValue="cambioGrupoSeleccionado"
          class="selector-grupo"
          solo
        ></v-combobox>
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
    </v-card>
    <v-card class="elevacion">
      <ListaCrudComponent
        :titulo="'Resumen de Sesiones'"
        :busqueda="false"
        :items="sesionesRealizadasGrupo"
        :cargando="cargandoSesionesGrupo"
        :permisoEdicion="false"
        :permisoCreacion="false"
        @detalle="navegarASesion"
      >
        <template v-slot:info-extra="{ item }">
          <div class="resultados">
            <p class="texto">RPE Medio: {{ item.rpeMedio }}</p>
            <p class="texto">Tiempo Medio: {{ item.tiempoMedio }}</p>
          </div>
          <v-icon :class="iconClass(item.rpeMedio)" class="icono-rpe">
            {{ getIcono(item.rpeMedio) }}
          </v-icon>
        </template>
      </ListaCrudComponent>
    </v-card>
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
    <v-dialog v-model="confirmarBorrado" max-width="600px">
      <v-card>
        <v-card-title class="flex-fila justify-space-between align-center">
          <span>Confirmar eliminar grupo</span>
          <v-btn
            aria-label="cancelar-cambio-encargado"
            icon
            @click="cancelarBorrarGrupo"
            flat
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          ¿Está seguro de que desea eliminar el grupo? Se eliminarán todas las
          solicitudes y las sesiones futuras del grupo.
        </v-card-text>
        <v-btn
          aria-label="confirmar-eliminar-grupo"
          class="claro boton"
          @click="borrarGrupo"
          >Confirmar</v-btn
        >
        <v-btn
          aria-label="cancelar-eliminar-grupo"
          class="rechazo boton"
          @click="cancelarBorrarGrupo"
          >Cancelar</v-btn
        >
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import DetalleUsuarioSesionesComponent from "@/components/DetalleUsuarioSesionesComponent.vue";
import { useGruposStore } from "@/store/gruposStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useAlertasStore } from "@/store/alertasStore.js";
import { mapState, mapActions } from "pinia";
export default {
  components: {
    ListaCrudComponent,
    DetalleUsuarioSesionesComponent,
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
      cargandoSesionesGrupo: false,
      sesionesRealizadasDeUsuario: [],
      sesionesRealizadasGrupo: [],
      mostrarDetalleUsuario: false,
      mostrarConfirmacionCambioEncargado: false,
      miembroSeleccionado: null,
      confirmarBorrado: false,
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
    ...mapActions(useUsuariosStore, ["renovarToken"]),
    ...mapActions(useGruposStore, [
      "getGruposEncargado",
      "getSolicitudes",
      "aceptarSolicitud",
      "rechazarSolicitud",
      "getMiembrosGrupo",
      "cambiarEncargado",
      "eliminarGrupo",
    ]),
    ...mapActions(useSesionesRealizadasStore, [
      "getSesionesRealizadasDeUsuarioyGrupo",
      "getResumenSesionesRealizadasDeGrupo",
    ]),
    ...mapActions(useAlertasStore, ["mostrarAlerta"]),

    getIcono(rpe) {
      if (rpe >= 1 && rpe <= 2) {
        return "mdi-emoticon-cool-outline";
      } else if (rpe >= 3 && rpe <= 4) {
        return "mdi-emoticon-happy-outline";
      } else if (rpe >= 5 && rpe <= 6) {
        return "mdi-emoticon-neutral-outline";
      } else if (rpe >= 7 && rpe <= 8) {
        return "mdi-emoticon-sad-outline";
      } else if (rpe >= 9 && rpe <= 10) {
        return "mdi-emoticon-sick-outline";
      } else {
        return null;
      }
    },

    iconClass(rpe) {
      if (rpe < 6) {
        return "claro";
      } else if (rpe === 6 || rpe === 7) {
        return "elevado";
      } else if (rpe >= 8) {
        return "rechazo";
      }
    },

    filtroGrupos(item, queryText) {
      const nombre = item.nombre.toLowerCase();
      const query = queryText.toLowerCase();
      return nombre.indexOf(query) > -1;
    },

    isSeleccionValida(value) {
      if (!value) return true;
      return this.gruposEncargado.some(
        (grupo) => grupo.nombre === value.nombre
      );
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
      this.cargandoSesionesGrupo = true;
      this.miembrosGrupoSeleccionado = await this.getMiembrosGrupo(grupoHref);
      this.sesionesRealizadasGrupo =
        await this.getResumenSesionesRealizadasDeGrupo(
          this.grupoSeleccionado.nombre
        );
      this.cargandoMiembros = false;
      this.cargandoSesionesGrupo = false;
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
        this.mostrarAlerta("Encargado cambiado con éxito", "success");
        await this.renovarToken();
      } catch {
        this.mostrarAlerta("No se ha podido cambiar el encargado", "error");
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

    navegarASesion(sesion) {
      this.$router.push("/sesiones/" + sesion.sesionId);
    },

    borradoDeGrupo() {
      this.confirmarBorrado = true;
    },
    cancelarBorrarGrupo() {
      this.confirmarBorrado = false;
    },
    async borrarGrupo() {
      await this.eliminarGrupo(this.grupoSeleccionado._links.self.href);
      await this.cargarDatos();
      await this.renovarToken();
      this.confirmarBorrado = false;
    },
  },

  async mounted() {
    this.cargarDatos();
  },
};
</script>
<style scoped>
.columna{
  flex-flow: column;
  gap:10px;
}
.contenedor-flex {
  align-items: start;
}

.selector {
  align-items: center !important;
  justify-content: space-between;
  gap:10px;
}

.miembros-grupo {
  width: 50%;
  margin: 10px;
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

.resultados {
  margin-left: 20px;
}

.icono-rpe {
  position: absolute;
  bottom: 10px;
  right: 10px;
  font-size: 36px;
  padding: 8px;
  border-radius: 50%;
  color: white;
}

.elevacion{
  padding: 10px;
  width: 100%
}

</style>
