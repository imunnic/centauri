<template>
  <v-container>
    <!-- https://vuejs.org/guide/built-ins/transition transiciones de componentes -->
    <transition name="fade">
      <v-alert v-if="mostrarAlerta" :type="tipoAlerta" dismissible class="alert-container ma-2">
        {{ mensajeAlerta }}
      </v-alert>
    </transition>

    <ListaCrudComponent :items="equipamientosRegistrados" :key="equipamientosKey" @editar="editarEquipamiento"
      @crear="crearEquipamiento" :mostrar-imagen="true" :imagen-predeterminada="imagenNoEncontrada"
      :cargando="cargando"
      :permisoCreacion="permisoCreacion">
      <template v-slot:info-extra="{ item }">
        <p class="texto"><b>Tipo</b>: {{ item.tipo }}</p class="texto">
      </template>
    </ListaCrudComponent>

    <EquipamientoFormComponent v-if="mostrarCrearForm" @cerrar="cerrarFormulario" :edicion="modoEdicion"
      :equipamiento="equipamientoSeleccionado" @guardar="guardarEquipamiento" />
  </v-container>
</template>
<script>
import configuracion from '@/configuracion.json';
import ListaCrudComponent from '@/components/comun/ListaCrudComponent.vue';
import EquipamientoFormComponent from '@/components/EquipamientoFormComponent.vue';
import { useUsuariosStore } from '@/store/usuariosStore.js';
import { useEquipamientosStore } from '@/store/equipamientosStore.js';
import { mapState, mapActions } from 'pinia';

export default {
  components: { ListaCrudComponent, EquipamientoFormComponent },
  data() {
    return {
      equipamientoSeleccionado: {},
      mostrarCrearForm: false,
      modoEdicion: false,
      equipamientosKey: 0, //forzar renderizado https://michaelnthiessen.com/force-re-render/
      mostrarAlerta: false,
      mensajeAlerta: '',
      tipoAlerta: 'success',
      cargando: false,
      imagenNoEncontrada: configuracion.urlImagenNoEncontrada
    };
  },
  computed: {
    ...mapState(useEquipamientosStore, ['equipamientosRegistrados']),
    ...mapState(useUsuariosStore, ['token', 'perfil']),
    permisoCreacion() {
      return this.perfil == 'ECEF';
    }
  },
  watch: {
    equipamientosRegistrados: {
      handler() {
        this.equipamientosKey += 1;
      },
      deep: true,
    },
  },
  methods: {
    ...mapActions(useEquipamientosStore, [
      'cargarEquipamientos',
      'resetEquipamiento',
      'agregarEquipamiento',
      'modificarEquipamiento'
    ]),
    resetFormulario() {
      this.equipamientoSeleccionado = {};
      this.modoEdicion = false;
    },
    mostrarAlertaTemporal(mensaje, tipo = 'success') {
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
        this.mostrarAlertaTemporal('Equipamiento modificado con éxito');

      } else {
        await this.agregarEquipamiento(equipamiento);
        this.mostrarAlertaTemporal('Equipamiento creado con éxito');

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
    cerrarFormulario() {
      this.mostrarCrearForm = false;
      this.resetFormulario();
    }
  },
  async created() {
    this.cargando = true;
    await this.cargarEquipamientos();
    this.cargando = false;
    this.resetEquipamiento(this.equipamientoSeleccionado);
  }
};
</script>
<style scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from, .fade-leave-to {
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
.texto{
  font-size: 17px;
}
</style>