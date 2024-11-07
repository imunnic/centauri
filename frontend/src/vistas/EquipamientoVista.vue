<template>
  <v-container>
<<<<<<< HEAD
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
=======
>>>>>>> main

    <ListaCrudComponent
      :items="equipamientosRegistrados"
      :key="equipamientosKey"
      @editar="editarEquipamiento"
      @crear="crearEquipamiento"
      @detalle="abrirDialogoDetalle"
      @eliminar="borrarEquipamiento"
      :mostrar-imagen="true"
      :descripcion="false"
      :imagen-predeterminada="imagenNoEncontrada"
      :cargando="cargando"
      :permisoCreacion="permisoCreacion"
    >
    </ListaCrudComponent>

    <EquipamientoFormComponent
      v-if="mostrarCrearForm"
      @cerrar="cerrarFormulario"
      :edicion="modoEdicion"
      :equipamiento="equipamientoSeleccionado"
      @guardar="guardarEquipamiento"
    />

    <EquipamientoDetalleComponent
      v-if="detalle"
      :equipamiento="equipamientoSeleccionado"
      @cerrar="cerrarDialogoDetalle"
    />
  </v-container>
</template>

<script>
import configuracion from "@/configuracion.json";
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import EquipamientoFormComponent from "@/components/EquipamientoFormComponent.vue";
import EquipamientoDetalleComponent from "@/components/EquipamientoDetalleComponent.vue";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useEquipamientosStore } from "@/store/equipamientosStore.js";
import { useAlertasStore } from "@/store/alertasStore.js";
import { mapState, mapActions } from "pinia";

export default {
  components: {
    ListaCrudComponent,
    EquipamientoFormComponent,
    EquipamientoDetalleComponent,
  },
  data() {
    return {
      equipamientoSeleccionado: {},
      mostrarCrearForm: false,
      modoEdicion: false,
      detalle: false,
      equipamientosKey: 0,
      mostrarAlerta: false,
      mensajeAlerta: "",
      tipoAlerta: "success",
      cargando: false,
      imagenNoEncontrada: configuracion.urlImagenNoEncontrada,
    };
  },
  computed: {
    ...mapState(useEquipamientosStore, ["equipamientosRegistrados"]),
<<<<<<< HEAD
    ...mapState(useUsuariosStore, ["token", "perfil"]),
    permisoCreacion() {
=======
    ...mapState(useUsuariosStore, ["token", "perfil", "isLogged"]),
    permisoCreacionEdicion() {
>>>>>>> main
      return this.perfil == "ECEF";
    },
  },
  methods: {
    ...mapActions(useUsuariosStore, ['renovarToken']),
    ...mapActions(useEquipamientosStore, [
      "cargarEquipamientos",
      "resetEquipamiento",
      "agregarEquipamiento",
      "modificarEquipamiento",
      "eliminarEquipamiento",
    ]),
    ...mapActions(useAlertasStore,['mostrarAlerta']),
    resetFormulario() {
      this.equipamientoSeleccionado = {};
      this.modoEdicion = false;
    },
<<<<<<< HEAD
    mostrarAlertaTemporal(mensaje, tipo = "success") {
      this.mensajeAlerta = mensaje;
      this.tipoAlerta = tipo;
      this.mostrarAlerta = true;

      setTimeout(() => {
        this.mostrarAlerta = false;
      }, 3000);
    },
    async guardarEquipamiento(equipamiento) {
      if (this.modoEdicion) {
        await this.modificarEquipamiento(equipamiento);
        this.mostrarAlertaTemporal("Equipamiento modificado con éxito");
      } else {
        await this.agregarEquipamiento(equipamiento);
        this.mostrarAlertaTemporal("Equipamiento creado con éxito");
=======
    async guardarEquipamiento(equipamiento) {
      if (this.modoEdicion) {
        await this.modificarEquipamiento(equipamiento);
        this.mostrarAlerta("Equipamiento modificado con éxito", "success");
        await this.renovarCredenciales();
      } else {
        await this.agregarEquipamiento(equipamiento);
        this.mostrarAlerta("Equipamiento creado con éxito", "success");
        await this.renovarCredenciales();
>>>>>>> main
      }
      await this.cargarEquipamientos();
      this.cerrarFormulario();
      this.equipamientosKey += 1;
    },
    crearEquipamiento() {
      this.modoEdicion = false;
      this.equipamientoSeleccionado = {};
      this.mostrarCrearForm = true;
    },
    editarEquipamiento(equipamiento) {
      this.equipamientoSeleccionado = { ...equipamiento };
      this.modoEdicion = true;
      this.mostrarCrearForm = true;
    },
    abrirDialogoDetalle(equipamiento) {
      this.equipamientoSeleccionado = equipamiento;
      this.detalle = true;
    },
    cerrarDialogoDetalle() {
      this.detalle = false;
    },
    async borrarEquipamiento(equipamiento) {
      await this.eliminarEquipamiento(equipamiento);
      this.cargarEquipamientos();
      this.equipamientosKey += 1;
      this.mostrarAlerta("Equipamiento eliminado con éxito", "success");
      await this.renovarCredenciales();
    },
    cerrarFormulario() {
      this.mostrarCrearForm = false;
      this.resetFormulario();
    },
    async renovarCredenciales(){
      if(this.isLogged){
        this.renovarToken();
      }
    }
  },
  async created() {
    this.cargando = true;
    await this.cargarEquipamientos();
    this.cargando = false;
    this.resetEquipamiento(this.equipamientoSeleccionado);
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
.texto {
  font-size: 17px;
}
</style>
