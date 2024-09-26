<template>
  <v-app>
    <v-main>
      <HeaderComponent @navegacion="mostrarNavegador" :aplicacion="aplicacion"></HeaderComponent>
      <NavegadorComponent ref="navegadorComponent" :menu-items-prop="menu"></NavegadorComponent>
    </v-main>
  </v-app>
</template>

<script>
import HeaderComponent from './components/comun/HeaderComponent.vue';
import NavegadorComponent from './components/comun/NavegadorComponent.vue';
import {useUsuariosStore} from './store/usuariosStore.js';
import {mapActions, mapState} from 'pinia';
  export default{
    components: { HeaderComponent, NavegadorComponent },
    data() {
      return {
        menu:[
          { title: 'Fichas', prependIcon: 'mdi-clipboard-list-outline', ruta: '/fichas' },
          { title: 'Ejercicios', prependIcon: 'mdi-run', ruta:'/ejercicios' },
          { title: 'Equipamiento', prependIcon: 'mdi-dumbbell', ruta:'/equipamientos' }
        
        ],
        aplicacion:{
          nombre: "Centauri",
          icono:"/centauri_verde.png"
        }
      }
    },
    computed:{
    ...mapState(useUsuariosStore,['token', 'isLogged'])
  },
  methods: {
    ...mapActions(useUsuariosStore,['peticionLogin']),
    mostrarNavegador() {
      this.$refs.navegadorComponent.mostrarNavegador();
    }
  },
  async created() {
    if (!this.isLogged) {
      await this.peticionLogin();
    }
  },
  }
</script>
