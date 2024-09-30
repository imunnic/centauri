<template>
  <FichaFormComponent></FichaFormComponent>
</template>

<script>
import FichaFormComponent from '../components/FichaFormComponent.vue';
import { useEjerciciosStore } from '@/store/ejerciciosStore.js';
import { useUsuariosStore } from '@/store/usuariosStore.js';
import { mapActions, mapState } from 'pinia';
export default {
  components: {
    FichaFormComponent
  },
  computed: {
    ...mapState(useUsuariosStore, ['token'])
  },
  data() {
    return {
    }
  }
  ,
  methods: {
    ...mapActions(useEjerciciosStore, ['cargarEjercicios', 'arrancarServicioEjercicios'])
  },
  async created() {
    this.arrancarServicioEjercicios(this.token);
    await this.cargarEjercicios();
  },

}
</script>

<style></style>