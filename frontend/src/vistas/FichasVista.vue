<template>
    <v-container>
      <div class="contenedor-flex justify-start">
        <v-switch
          v-model="soloPendientes"
          label="Mostrar pendientes"
          color="red"
          active-color="red"
          class="interruptor"
        ></v-switch>
      </div>
      
      <ListaCrudComponent
        :items="fichasFiltradas"
        :key="fichasKey"
        @detalle="verFicha"
        :permiso-creacion="false"
        :cargando="cargando"
      />
    </v-container>
  </template>
  
  <script>
  import BuscadorComponent from '@/components/comun/BuscadorComponent.vue';
  import ListaCrudComponent from '../components/comun/ListaCrudComponent.vue';
  import { useFichasStore } from '@/store/fichasStore.js';
  import { mapState } from 'pinia';
  
  export default {
    components: { BuscadorComponent, ListaCrudComponent },
    computed: {
      ...mapState(useFichasStore, ['fichasRegistradas']),
      fichasFiltradas() {
        return this.soloPendientes
          ? this.fichasRegistradas.filter(ficha => ficha.estado === 'PENDIENTE')
          : this.fichasRegistradas;
      }
    },
    data() {
      return {
        fichasKey: 0,
        detalle: false,
        modoEdicion: false,
        misActions: [],
        cargando: false,
        soloPendientes: false
      };
    },
    created() {
      this.cargando = true;
      this.cargando = false;
    },
    methods: {
      updateFichasFiltradas(fichasFiltradas) {
        this.fichasFiltradas = fichasFiltradas;
      },
      verFicha(ficha) {
        console.log(ficha);
        this.$router.push('/fichas/' + ficha.id);
      }
    }
  };
  </script>
  
  <style scoped>
  .contenedor-flex{
    padding: 0;
    margin: 5px;
  }
  .interruptor .v-input--selection-controls__input{
    color: red;
  }
  .v-input__details{
    padding: 0 !important;
    margin: 0 !important;
  }
  </style>
  