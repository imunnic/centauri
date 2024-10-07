<template>
  <div class="contenedor flex-fila">
    <div v-if="isLargeScreen" class="contenedor grupos izquierda">
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
    </div>
    <div class="derecha">
      <CalendarioComponent
        @fecha-seleccionada="nuevaSesion"
        @borrarSesion="borrarSesion"
        @editarSesion="editarSesion"
        @detalle="navegarADetalleSesion"
        :modoInicial="modoInicial"
        :sesiones="sesiones"
        :gruposConPermiso="gruposEncargado"
        class="calendario"
      ></CalendarioComponent>

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
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import CalendarioComponent from "@/components/comun/CalendarioComponent.vue";
import SesionFormComponent from "@/components/SesionFormComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useUsuariosStore } from "@/store/usuariosStore";
import { mapActions, mapState } from "pinia";
import grupos from "@/assets/grupos.json";

export default {
  components: {
    ListaCrudComponent,
    CalendarioComponent,
    SesionFormComponent,
  },
  computed: {
    ...mapState(useUsuariosStore, ["username"]),
    isLargeScreen() {
      return this.windowWidth > 1500;
    },
    modoInicial() {
      return this.isLargeScreen ? "mes" : "dia";
    },
  },
  data() {
    return {
      windowWidth: window.innerWidth,
      gruposUsuario: [
        { nombre: "Grupo 1", color: { nombre: "rojo", valor: "#FF0000" } },
        { nombre: "Grupo 2", color: { nombre: "azul", valor: "#0000FF" } },
      ],
      gruposEncargado: [],
      fechaSeleccionada: null,
      sesiones: [],
      mostrarFormulario: false,
      edicion:false,
      sesionSeleccionada: null,
    };
  },
  methods: {
    ...mapActions(useSesionesStore, [
      "crearSesion",
      "cargarSesiones",
      "eliminarSesion",
      "modificarSesion"
    ]),
    nuevaSesion(fecha) {
      if(this.gruposEncargado.length >= 1){
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
    async sesionEditada(sesion){
      console.log(sesion);
      await this.modificarSesion(sesion)
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
    navegarADetalleSesion(sesionHref) {
      let id = sesionHref.split("/").pop();
      this.$router.push("/sesiones/" + id);
    }
  },
  async created() {
    let misGrupos = grupos.filter((grupo) =>
      grupo.miembros.includes(this.username)
    );
    this.gruposUsuario = misGrupos;
    let sesiones = await this.cargarSesiones(this.gruposUsuario);
    this.sesiones = sesiones;
  },
  mounted() {
    window.addEventListener("resize", this.handleResize);
    this.gruposEncargado = this.filtrarPorEncargado(this.username);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleResize);
  },
};
</script>

<style scoped>
.texto {
  padding-top: 10px;
  padding-left: 10px;
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
  background-color: var(--bg-color);
}
</style>
