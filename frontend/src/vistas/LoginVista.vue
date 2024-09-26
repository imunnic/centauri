<template>
  <div>

  </div>
</template>

<script>
import { useUsuariosStore } from '@/store/usuariosStore.js';
import { useFichasStore } from '@/store/fichasStore.js';
import { useEjerciciosStore } from '@/store/ejerciciosStore.js';
import { useEquipamientosStore } from '@/store/equipamientosStore.js';
import { mapState, mapActions } from 'pinia';
export default {

  computed: {
    ...mapState(useUsuariosStore, ['token', 'isLogged'])
  },
  data() {
    return {

    }
  },
  methods: {
    ...mapActions(useUsuariosStore, ['peticionLogin']),
    ...mapActions(useFichasStore, ['arrancarServicioFicha']),
    ...mapActions(useEjerciciosStore, ['arrancarServicioEjercicios']),
    ...mapActions(useEquipamientosStore,['arrancarServicioEquipamientos']),
    redirigirConRetardo() {
      setTimeout(() => {
        this.$router.push('/fichas');
      }, 1000);
    }

  },
  async created() {
    try {
      await this.peticionLogin();
    } catch (error) {
      console.error('Error during login:', error);
    }
    this.arrancarServicioFicha(this.token);
    this.arrancarServicioEjercicios(this.token);
    this.arrancarServicioEquipamientos(this.token);
    this.redirigirConRetardo();
  }
}
</script>
<style></style>