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
        :modoInicial="modoInicial"
        :sesiones="sesiones"
        :gruposConPermiso="gruposEncargado"
        class="calendario"
      ></CalendarioComponent>

      <SesionFormComponent
        v-if="edicion"
        :fecha="fechaSeleccionada"
        :grupos="gruposEncargado"
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
    ...mapState(useUsuariosStore, ['username']),
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
      edicion: false,
    };
  },
  methods: {
    ...mapActions(useSesionesStore, ['crearSesion', 'cargarSesiones', 'eliminarSesion']),
    nuevaSesion(fecha) {
      this.fechaSeleccionada = fecha;
      this.edicion = true;
    },
    async sesionCreada(nuevaSesion) {
      await this.crearSesion(nuevaSesion);
      this.edicion = false;
      let sesiones = await this.cargarSesiones(this.gruposUsuario);
      this.sesiones = sesiones;
    },
    cerrarFormularioSesion() {
      this.edicion = false;
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
    async borrarSesion(href){
      await this.eliminarSesion(href);
      let sesiones = await this.cargarSesiones(this.gruposUsuario);
      this.sesiones = sesiones;
    },
  },
  async created(){
    let misGrupos = grupos.filter(grupo => grupo.miembros.includes(this.username));
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
