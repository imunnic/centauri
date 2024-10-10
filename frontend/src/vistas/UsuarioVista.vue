<template>
  <div class="contenedor flex-fila">
    <div v-if="isPantallaGrande" class="contenedor grupos izquierda">
      <b>Grupos:</b>
      <v-card
        v-for="(item, index) in gruposUsuario"
        :key="index"
        class="carta contenedor"
        elevation="2"
        :style="getCartaStyle(item)"
      >
        <p class="texto">{{ item.nombre }}</p>
      </v-card>

      <b>Encargado de:</b>
      <v-card
        v-for="(item, index) in gruposEncargado"
        :key="index"
        class="carta contenedor"
        elevation="2"
        :style="getCartaStyle(item)"
      >
        <p class="texto">{{ item.nombre }}</p>
      </v-card>
      <b>Últimas sesiones:</b>
      <v-card
        v-for="(item, index) in sesionesRealizadasRegistradas"
        :key="index"
        class="carta contenedor"
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
          {{ getIcon(item.rpe) }}
        </v-icon>
      </v-card>
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
    </div>
  </div>
</template>

<script>
import DetalleSesionComponent from "@/components/DetalleSesionComponent.vue";
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import CalendarioComponent from "@/components/comun/CalendarioComponent.vue";
import SesionFormComponent from "@/components/SesionFormComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore";
import { mapActions, mapState } from "pinia";
import grupos from "@/assets/grupos.json";

export default {
  components: {
    ListaCrudComponent,
    CalendarioComponent,
    SesionFormComponent,
    DetalleSesionComponent,
  },
  computed: {
    ...mapState(useUsuariosStore, ["username", "href"]),
    ...mapState(useSesionesRealizadasStore, ["sesionesRealizadasRegistradas"]),
    isPantallaGrande() {
      return this.anchoPantalla > 1500;
    },
    modoInicial() {
      return this.isPantallaGrande ? "mes" : "dia";
    },
  },
  data() {
    return {
      anchoPantalla: window.innerWidth,
      gruposUsuario: [
        { nombre: "Grupo 1", color: { nombre: "rojo", valor: "#FF0000" } },
        { nombre: "Grupo 2", color: { nombre: "azul", valor: "#0000FF" } },
      ],
      gruposEncargado: [],
      fechaSeleccionada: null,
      sesiones: [],
      mostrarFormulario: false,
      edicion: false,
      sesionSeleccionada: null,
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
      let sesiones = await this.cargarSesiones(this.gruposUsuario);
      this.sesiones = sesiones;
    },
    cerrarFormularioSesion() {
      this.mostrarFormulario = false;
    },
    getCartaStyle(item) {
      return {
        "--bg-color": item.color.valor,
      };
    },
    filtrarPorEncargado(encargado) {
      let filtro = grupos.filter((grupo) => grupo.encargado === encargado);
      return filtro;
    },
    async borrarSesion(href) {
      await this.eliminarSesion(href);
      let sesiones = await this.cargarSesiones(this.gruposUsuario);
      this.sesiones = sesiones;
    },
    editarSesion(sesion) {
      this.edicion = true;
      this.fechaSeleccionada = this.formatoFechaEdicion(sesion.fecha);
      this.sesionSeleccionada = sesion;
      this.mostrarFormulario = true;
    },
    async sesionEditada(sesion) {
      console.log(sesion);
      await this.modificarSesion(sesion);
      this.edicion = false;
      let sesiones = await this.cargarSesiones(this.gruposUsuario);
      this.sesiones = sesiones;
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
    navegarADetalleSesion(sesionHref) {
      let id = sesionHref.split("/").pop();
      this.$router.push("/sesiones/" + id);
    },
    seleccionarSesion(sesion) {
      this.sesionSeleccionada = sesion;
    },
    async sesionRealizada(sesion) {
      sesion.usuario = this.href;
      await this.crearSesionRealizada(sesion);
      this.$refs.calendario.mostrarTarjeta =
        !this.$refs.calendario.mostrarTarjeta;
      await this.cargarSesionesRealizadas(this.href);
    },
    getIcon(rpe) {
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
        return "icono-verde";
      } else if (rpe === 6 || rpe === 7) {
        return "icono-naranja";
      } else if (rpe >= 8) {
        return "icono-rojo";
      }
    },
  },
  async created() {
    let misGrupos = grupos.filter((grupo) =>
      grupo.miembros.includes(this.username)
    );
    this.gruposUsuario = misGrupos;
    let sesiones = await this.cargarSesiones(this.gruposUsuario);
    this.sesiones = sesiones;
    await this.cargarSesionesRealizadas(this.href);
  },
  mounted() {
    window.addEventListener("resize", this.manejarCambioTamano);
    this.gruposEncargado = this.filtrarPorEncargado(this.username);
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

.grupos {
  max-width: 20vw;
}

.carta::before {
  width: 5px;
  background-color: var(--claro);
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

.icono-verde {
  background-color: var(--suave);
}

.icono-naranja {
  background-color: var(--elevado);
}

.icono-rojo {
  background-color: var(--rechazo);
}
</style>
