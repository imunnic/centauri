<template>
  <v-container>
    <h2>Ver Ficha</h2>
    <div class="contenedor-flex">
      <v-text-field label="Nombre de la ficha" v-model="fichaSeleccionada.nombre" readonly
        class="input-field"></v-text-field>
      <v-text-field label="Tipo de ficha" v-model="fichaSeleccionada.tipo" readonly class="input-field"></v-text-field>
      <v-text-field label="Parte del entrenamiento" v-model="fichaSeleccionada.parte" readonly
        class="input-field"></v-text-field>
    </div>
    <FichaComponent :rondas="fichaSeleccionada.rutina.rondas" :solo-lectura="true" />

  </v-container>
</template>

<script>
import FichaComponent from '@/components/FichaComponent.vue';
import { useFichasStore } from '../store/fichasStore.js';
import { mapState } from 'pinia';

export default {
  components: {
    FichaComponent
  },
  data() {
    return {
      fichaSeleccionada: null
    };
  },
  computed: {
    ...mapState(useFichasStore, ['fichasRegistradas'])
  },
  methods: {
    aprobarEntrenamiento() {

    }
  },
  created() {
    this.fichaSeleccionada = this.fichasRegistradas.find(ficha => ficha.id == this.$route.params.id);
  }
};
</script>

<style scoped>
.contenedor-flex {
  gap: 10px;
  margin-bottom: 20px;
}

.input-field {
  max-width: 300px;
}
</style>