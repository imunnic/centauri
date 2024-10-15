<template>
  <v-container class="contenedor-flex">
    <ListaCrudComponent
      :busqueda="false"
      titulo="Solicitudes de acceso"
      :accionesPersonalizadas="acciones"
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
      <v-select
        label="Grupo"
        :items="gruposEncargado"
        item-title="nombre"
        :return-object="true"
        @update:modelValue="cambioGrupoSeleccionado"
        class="selector-grupo"
      >
      </v-select>
      <ListaCrudComponent
        :busqueda="false"
        :items="miembrosGrupoSeleccionado"
        :cargando="cargandoMiembros"
        :permisoEdicion="false"
        :permisoCreacion="false"
        @detalle="verMiembro"
      >
      </ListaCrudComponent>
      <DetalleUsuarioSesiones
        v-if="mostrarDetalleUsuario"
        :sesionesRealizadas="sesionesRealizadasDeUsuario"
        @cerrar="cerrarMiembro"
      ></DetalleUsuarioSesiones>
    </div>
  </v-container>
</template>
<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import DetalleUsuarioSesiones from "@/components/DetalleUsuarioSesiones.vue";
import { useGruposStore } from "@/store/gruposStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { mapState, mapActions } from "pinia";
export default {
  components: { ListaCrudComponent, DetalleUsuarioSesiones },
  data() {
    return {
      acciones: [
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
      cargando: false,
      grupoSeleccionado: "",
      miembrosGrupoSeleccionado: [],
      cargandoMiembros: false,
      sesionesRealizadasDeUsuario: [],
      mostrarDetalleUsuario: false,
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
    ]),
    ...mapActions(useSesionesRealizadasStore, [
      "getSesionesRealizadasDeUsuarioyGrupo",
    ]),
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
    async cambioGrupoSeleccionado(grupo) {
      this.grupoSeleccionado = grupo;
      let grupoHref = this.grupoSeleccionado._links.self.href;
      this.cargandoMiembros = true;
      this.miembrosGrupoSeleccionado = await this.getMiembrosGrupo(grupoHref);
      this.cargandoMiembros = false;
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
  },
  async mounted() {
    this.cargando = true;
    await this.getGruposEncargado(this.href);
    await this.getSolicitudes();
    this.cargando = false;
  },
};
</script>
<style scoped>
.contenedor-flex {
  align-items: start;
}
.miembros-grupo {
  width: 50%;
}
.solicitudes {
  width: 50%;
}
.selector-grupo {
  width: 25%;
  min-width: 200px;
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
