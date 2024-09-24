<template>
  <FichaFormComponent></FichaFormComponent>
</template>

<script>
import FichaFormComponent from '../components/FichaFormComponent.vue';
import { useEjerciciosStore } from '@/store/ejerciciosStore.js'
import { mapActions } from 'pinia'
export default {
  components: {
    FichaFormComponent
  },
  data() {
    return {
    }
  }
  ,
  methods: {
    ...mapActions(useEjerciciosStore, ['cargarEjercicios', 'arrancarServicioEjercicios'])
  },
  created() {
    this.arrancarServicioEjercicios();
    this.cargarEjercicios();
  },

}
</script>

<style></style>