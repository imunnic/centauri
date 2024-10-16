<template>
  <v-container>
    <MensajeAlertaComponent
      :mostrar="mostrarAlerta"
      :mensaje="mensajeAlerta"
      :tipo="tipoAlerta"
      @cerrar="mostrarAlerta = false"
    ></MensajeAlertaComponent>

    <ListaCrudComponent
      :items="ejerciciosRegistrados"
      :key="ejerciciosKey"
      @editar="editarEjercicio"
      @detalle="abrirDialogoDetalle"
      @crear="crearEjercicio"
      :cargando="cargando"
      @eliminar="borrarEjercicio"
      :permisoCreacion="permisoCreacion"
    >
    </ListaCrudComponent>

    <EjercicioFormComponent
      v-if="mostrarForm"
      @cerrar="cerrarForm"
      :edicion="modoEdicion"
      :ejercicio="ejercicioSeleccionado"
      @guardar="guardarEjercicio"
    />

    <EjercicioDetalleComponent
      v-if="detalle"
      :ejercicio="ejercicioSeleccionado"
      @cerrar="cerrarDialogoDetalle"
    />
  </v-container>
</template>

<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import EjercicioFormComponent from "@/components/EjercicioFormComponent.vue";
import EjercicioDetalleComponent from "@/components/EjercicioDetalleComponent.vue";
import MensajeAlertaComponent from "@/components/comun/MensajeAlertaComponent.vue";
import { mapState, mapActions } from "pinia";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useEjerciciosStore } from "@/store/ejerciciosStore.js";

export default {
  components: {
    ListaCrudComponent,
    EjercicioFormComponent,
    EjercicioDetalleComponent,
    MensajeAlertaComponent,
  },
  data() {
    return {
      ejercicioSeleccionado: {},
      mostrarForm: false,
      modoEdicion: false,
      detalle: false,
      ejerciciosKey: 0, //forzar renderizado https://michaelnthiessen.com/force-re-render/
      mostrarAlerta: false,
      mensajeAlerta: "",
      tipoAlerta: "success",
      cargando: false,
    };
  },
  computed: {
    ...mapState(useUsuariosStore, ["token", "perfil"]),
    ...mapState(useEjerciciosStore, ["ejerciciosRegistrados"]),
    permisoCreacion() {
      return this.perfil == "ECEF";
    },
  },
  watch: {
    ejerciciosRegistrados: {
      handler() {
        this.ejerciciosKey += 1;
      },
      deep: true,
    },
  },
  methods: {
    ...mapActions(useEjerciciosStore, [
      "arrancarServicioEjercicios",
      "cargarEjercicios",
      "resetEjercicio",
      "modificarEjercicio",
      "eliminarEjercicio",
      "agregarEjercicio",
    ]),
    resetFormulario() {
      this.ejercicioSeleccionado = {};
      this.modoEdicion = false;
    },
    mostrarAlertaTemporal(mensaje, tipo) {
      this.mensajeAlerta = mensaje;
      this.tipoAlerta = tipo;
      this.mostrarAlerta = true;
    },
    async guardarEjercicio(ejercicio) {
      if (this.modoEdicion) {
        await this.modificarEjercicio(ejercicio);
        this.mostrarAlertaTemporal("Ejercicio modificado con éxito", "success");
      } else {
        await this.agregarEjercicio(ejercicio);
        this.mostrarAlertaTemporal("Ejercicio creado con éxito", "success");
      }
      await this.cargarEjercicios();
      this.cerrarForm();
      this.ejerciciosKey += 1;
    },
    cerrarForm() {
      this.modoEdicion = false;
      this.mostrarForm = false;
      this.resetFormulario();
    },
    crearEjercicio() {
      this.modoEdicion = false;
      this.ejercicioSeleccionado = {};
      this.mostrarForm = true;
    },
    editarEjercicio(ejercicio) {
      this.ejercicioSeleccionado = { ...ejercicio };
      this.modoEdicion = true;
      this.mostrarForm = true;
    },
    abrirDialogoDetalle(ejercicio) {
      this.ejercicioSeleccionado = ejercicio;
      this.detalle = true;
    },
    cerrarDialogoDetalle() {
      this.detalle = false;
    },
    async borrarEjercicio(ejercicio) {
      await this.eliminarEjercicio(ejercicio);
      this.cargando = true;
      await this.cargarEjercicios();
      this.cargando = false;
      this.mostrarAlertaTemporal("Ejercicio eliminado con éxito");
      this.ejerciciosKey += 1;
    },
  },
  async created() {
    this.cargando = true;
    this.arrancarServicioEjercicios(this.token);
    await this.cargarEjercicios();
    this.cargando = false;
    this.resetEjercicio(this.ejercicioSeleccionado);
  },
};
</script>

<style scoped>
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

.componente {
  margin-bottom: 20px !important;
  width: 100%;
}
</style>
