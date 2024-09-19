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
import { useUsuariosStore } from '@/store/usuariosStore.js';
import { mapActions, mapState } from 'pinia';
import axios from 'axios';

export default {
  components: { BuscadorComponent, ListaCrudComponent },
  computed: {
    ...mapState(useFichasStore, ['fichasRegistradas']),
    ...mapState(useUsuariosStore, ['token']),
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
  async mounted() {
    this.arrancarServicioFicha(this.token);
    this.cargando = true;
    console.log(this.token);
    let response = await axios.get("http://localhost:8080/api/fichas", {
    headers: {
        'Authorization': `Bearer ${this.token}` // Añadir el token en los headers
    }
    })  
    console.log(response);
    this.cargando = false;
  },
  methods: {
    ...mapActions(useFichasStore, ['arrancarServicioFicha', 'cargarFichas']),
    verFicha(ficha) {
      this.$router.push('/fichas/' + ficha.id);
    }
  }
};
</script>
