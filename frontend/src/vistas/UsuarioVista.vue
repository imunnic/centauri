<template>
  <MensajeAlertaComponent
    :mostrar="mostrarAlerta"
    :mensaje="mensajeAlerta"
    :tipo="tipoAlerta"
    @cerrar="mostrarAlerta = false"
  ></MensajeAlertaComponent>
  <div class="contenedor-flex agenda">
    <div class="contenedor grupos izquierda">
      <v-card elevation="2" v-if="isPantallaGrande" class="grupos">
        <v-card-title>Grupos:</v-card-title>
        <v-card
          v-for="(item, index) in gruposRegistrados"
          :key="index"
          class="carta contenedor grupo"
          elevation="2"
          :style="getCartaStyle(item)"
        >
          <p class="texto">{{ item.nombre }}</p>
        </v-card>
        <FabBotonComponent
          class="boton"
          :texto="'Nueva Solicitud'"
          @click="abrirFormSolicitud"
        ></FabBotonComponent>
      </v-card>
      <v-card elevation="2" v-if="isPantallaGrande" class="grupos">
        <v-card-title>Encargado de:</v-card-title>
        <v-card
          v-for="(item, index) in gruposEncargado"
          :key="index"
          class="carta contenedor grupo"
          elevation="2"
          :style="getCartaStyle(item)"
        >
          <p class="texto">{{ item.nombre }}</p>
        </v-card>
        <FabBotonComponent
          class="boton"
          :texto="'Nuevo grupo'"
          @click="abrirFormGrupo"
        ></FabBotonComponent>
      </v-card>
      <div>
        <b>Últimas sesiones:</b>
        <v-card
          v-for="(item, index) in sesionesRealizadasRegistradas"
          :key="index"
          class="carta contenedor ultimas-sesiones"
          elevation="2"
          @click="navegarADetalleSesion(item.sesionId)"
        >
          <b
            ><div class="contenedor-flex">
              <p class="texto">{{ item.nombreSesion }}</p>
              <p class="texto">
                {{ formatoFechaConBarra(item.fechaSesion) }}
              </p>
            </div></b
          >
          <div class="contenedor-flex resultados">
            <p class="texto">RPE: {{ item.rpe }}</p>
            <p class="texto">Tiempo: {{ item.tiempo }}</p>
            <p class="texto">Comentarios: {{ item.comentarios }}</p>
          </div>
          <v-icon :class="iconClass(item.rpe)" class="icono-rpe">
            {{ getIcono(item.rpe) }}
          </v-icon>
        </v-card>
      </div>
    </div>
    <div class="derecha">
      <CalendarioComponent
        ref="calendario"
        @fecha-seleccionada="nuevaSesion"
        @sesion-seleccionada="seleccionarSesion"
        @borrarSesion="borrarSesion"
        @editarSesion="editarSesion"
        :modoInicial="modoInicial"
        :sesiones="sesiones"
        :gruposConPermiso="gruposEncargado"
        class="calendario"
      >
        <template v-slot:detalle-sesion>
          <DetalleSesionComponent
            :sesion="sesionSeleccionada"
            :gruposConPermiso="gruposEncargado"
            @editarSesion="editarSesion"
            @borrarSesion="borrarSesion"
            @detalle="navegarADetalleSesion"
            @hecha="sesionRealizada"
          />
        </template>
      </CalendarioComponent>

      <SesionFormComponent
        v-if="mostrarFormulario"
        :fecha="fechaSeleccionada"
        :grupos="gruposEncargado"
        :sesion="sesionSeleccionada"
        :edicion="edicion"
        @sesionEditada="sesionEditada"
        @sesionCreada="sesionCreada"
        @cerrar="cerrarFormularioSesion"
      />

      <GrupoFormComponent
        v-if="mostrarFormularioGrupo"
        @crear-grupo="nuevoGrupo"
        @cerrar="cerrarGrupo"
      >
      </GrupoFormComponent>

      <SolicitudFormComponent
        v-if="mostrarFormularioSolicitud"
        :grupos="gruposSinUsuario"
        @solicitar-acceso="nuevaSolicitud"
        @cerrar="cerrarFormSolicitud"
      ></SolicitudFormComponent>
    </div>
  </div>
</template>

<script>
import DetalleSesionComponent from "@/components/DetalleSesionComponent.vue";
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import CalendarioComponent from "@/components/comun/CalendarioComponent.vue";
import SesionFormComponent from "@/components/SesionFormComponent.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";
import GrupoFormComponent from "@/components/GrupoFormComponent.vue";
import SolicitudFormComponent from "@/components/SolicitudFormComponent.vue";
import MensajeAlertaComponent from "@/components/comun/MensajeAlertaComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useGruposStore } from "@/store/gruposStore.js";
import { mapActions, mapState } from "pinia";
import grupos from "@/assets/grupos.json";
import configuracion from "@/configuracion.json";

export default {
  components: {
    ListaCrudComponent,
    CalendarioComponent,
    SesionFormComponent,
    DetalleSesionComponent,
    FabBotonComponent,
    GrupoFormComponent,
    SolicitudFormComponent,
    MensajeAlertaComponent,
  },
  computed: {
    ...mapState(useUsuariosStore, ["username", "href", "id"]),
    ...mapState(useSesionesRealizadasStore, ["sesionesRealizadasRegistradas"]),
    ...mapState(useGruposStore, ["gruposRegistrados", "gruposEncargado"]),
    isPantallaGrande() {
      return this.anchoPantalla > 1500;
    },
    modoInicial() {
      return this.anchoPantalla > 1000 ? "mes" : "dia";
    },
  },
  data() {
    return {
      anchoPantalla: window.innerWidth,
      gruposSinUsuario: [],
      fechaSeleccionada: null,
      sesiones: [],
      mostrarFormulario: false,
      mostrarFormularioGrupo: false,
      mostrarFormularioSolicitud: false,
      edicion: false,
      sesionSeleccionada: null,
      mostrarAlerta: false,
      mensajeAlerta: "",
      tipoAlerta: "error",
    };
  },
  methods: {
    ...mapActions(useSesionesStore, [
      "crearSesion",
      "cargarSesiones",
      "eliminarSesion",
      "modificarSesion",
    ]),
    ...mapActions(useSesionesRealizadasStore, [
      "crearSesionRealizada",
      "cargarSesionesRealizadas",
    ]),
    ...mapActions(useGruposStore, [
      "crearGrupo",
      "getGruposSinUsuario",
      "realizarSolicitud",
      "getGruposUsuario",
      "getGruposEncargado",
    ]),
    mostrarAlertaTemporal(mensaje, tipo) {
      this.mensajeAlerta = mensaje;
      this.tipoAlerta = tipo;
      this.mostrarAlerta = true;
    },
    nuevaSesion(fecha) {
      if (this.gruposEncargado.length >= 1) {
        this.sesionSeleccionada = {};
        this.fechaSeleccionada = fecha;
        this.mostrarFormulario = true;
        this.edicion = false;
      }
    },

    async sesionCreada(nuevaSesion) {
      await this.crearSesion(nuevaSesion);
      this.mostrarFormulario = false;
      let sesiones = await this.cargarSesiones(this.gruposRegistrados);
      this.sesiones = sesiones;
    },

    cerrarFormularioSesion() {
      this.mostrarFormulario = false;
    },

    async borrarSesion(href) {
      await this.eliminarSesion(href);
      let sesiones = await this.cargarSesiones(this.gruposRegistrados);
      this.sesiones = sesiones;
    },

    editarSesion(sesion) {
      this.edicion = true;
      this.fechaSeleccionada = this.formatoFechaEdicion(sesion.fecha);
      this.sesionSeleccionada = sesion;
      this.mostrarFormulario = true;
    },

    async sesionEditada(sesion) {
      await this.modificarSesion(sesion);
      this.edicion = false;
      let sesiones = await this.cargarSesiones(this.gruposRegistrados);
      this.sesiones = sesiones;
    },

    async sesionRealizada(sesion) {
      sesion.sesion = {
        id: sesion.sesion.split("/").pop(),
      };
      sesion.usuario = { id: this.id };
      await this.crearSesionRealizada(sesion);
      this.$refs.calendario.mostrarTarjeta =
        !this.$refs.calendario.mostrarTarjeta;
      await this.cargarSesionesRealizadas(this.href);
    },

    seleccionarSesion(sesion) {
      this.sesionSeleccionada = sesion;
    },

    navegarADetalleSesion(sesionHref) {
      let id = sesionHref.split("/").pop();
      this.$router.push("/sesiones/" + id);
    },

    filtrarPorEncargado(encargado) {
      let filtro = grupos.filter((grupo) => grupo.encargado === encargado);
      return filtro;
    },

    formatoFechaEdicion(fecha) {
      const partes = fecha.split("/");
      const dia = parseInt(partes[0], 10);
      const mes = parseInt(partes[1], 10) - 1;
      const ano = parseInt(partes[2], 10);
      return new Date(ano, mes, dia);
    },

    formatoFechaConBarra(fecha) {
      let partesFecha = fecha.split("-");
      let nuevaFecha =
        partesFecha[2] + "/" + partesFecha[1] + "/" + partesFecha[0];
      return nuevaFecha;
    },

    getCartaStyle(item) {
      return {
        "--bg-color": item.color,
      };
    },

    getIcono(rpe) {
      if (rpe < 6) {
        return "mdi-speedometer-slow";
      } else if (rpe === 6 || rpe === 7) {
        return "mdi-speedometer-medium";
      } else if (rpe >= 8) {
        return "mdi-speedometer";
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

    manejarCambioTamano() {
      this.anchoPantalla = window.innerWidth;
    },

    abrirFormGrupo() {
      this.mostrarFormularioGrupo = true;
    },
    cerrarGrupo() {
      this.mostrarFormularioGrupo = false;
    },
    async nuevoGrupo(grupo) {
      grupo.encargado = this.href;
      grupo.miembros = [this.href];
      try {
        await this.crearGrupo(grupo);
        this.mostrarAlertaTemporal("Solicitud realizada con éxito", "success");
        this.getGruposEncargado(this.href);
        this.getGruposUsuario(this.href);
      } catch (error) {
        this.mostrarAlertaTemporal(
          "No se ha podido realizar la solicitud",
          "error"
        );
      }
    },
    async abrirFormSolicitud() {
      let response = await this.getGruposSinUsuario(this.id);
      this.gruposSinUsuario = response.data;
      this.mostrarFormularioSolicitud = true;
    },
    cerrarFormSolicitud() {
      this.mostrarFormularioSolicitud = false;
      this.gruposSinUsuario = [];
    },
    async nuevaSolicitud(grupoId) {
      let solicitud = {
        grupo: configuracion.urlBase + "grupos/" + grupoId,
        usuario: this.href,
      };
      try {
        await this.realizarSolicitud(solicitud);
        this.mostrarAlertaTemporal("Solicitud realizada con éxito", "success");
      } catch (error) {
        this.mostrarAlertaTemporal(
          "No se ha podido realizar la solicitud",
          "error"
        );
      }
    },
  },
  async created() {
    await this.getGruposUsuario(this.href);
    await this.getGruposEncargado(this.href);
    const [sesiones] = await Promise.all([
      this.cargarSesiones(this.gruposRegistrados),
      this.cargarSesionesRealizadas(this.href),
    ]);
    this.sesiones = sesiones;
  },
  async mounted() {
    window.addEventListener("resize", this.manejarCambioTamano);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.manejarCambioTamano);
  },
};
</script>

<style scoped>
.texto {
  padding-top: 10px;
  padding-left: 10px;
}

.contenedor-flex {
  align-items: start;
  justify-content: space-between;
  padding: 10px;
}

.resultados {
  flex-flow: column;
  justify-content: start;
}

.flex-fila {
  justify-content: space-between;
  gap: 20px;
}

.izquierda {
  width: 100%;
}

.derecha {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  min-width: 50%;
  padding-left: 20px;
}

.grupo {
  margin-bottom: 5px !important;
}

.grupos {
  max-width: 20vw;
  display: flex;
  flex-flow: column;
  padding: 10px;
  margin-bottom: 10px;
}

.carta::before {
  width: 5px;
  background-color: var(--bg-color);
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

.carta.ultimas-sesiones::before {
  background-color: var(--claro);
}

.boton {
  margin-top: 15px;
  position: relative !important;
  align-self: flex-end;
  width: fit-content;
  border-radius: 10px;
  padding: 5px 5px;
  height: fit-content;
}

@media (max-width: 1500px) {
  .agenda {
    flex-flow: column-reverse;
    align-items: center;
  }

  .grupos {
    max-width: 1200px;
    padding: 16px;
  }

  .izquierda {
    width: 100% !important;
  }

  .derecha {
    width: 100%;
    padding-left: 0;
  }
}
</style>
