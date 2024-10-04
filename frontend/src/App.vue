<template>
  <v-app>
    <v-main>
      <HeaderComponent
        @navegacion="mostrarNavegador"
        :aplicacion="aplicacion"
      ></HeaderComponent>
      <NavegadorComponent
        ref="navegadorComponent"
        :menu-items-prop="menuItems"
      ></NavegadorComponent>
    </v-main>
  </v-app>
</template>

<script>
import HeaderComponent from "./components/comun/HeaderComponent.vue";
import NavegadorComponent from "./components/comun/NavegadorComponent.vue";
import { useUsuariosStore } from "./store/usuariosStore.js";
import { mapActions, mapState } from "pinia";
export default {
  components: { HeaderComponent, NavegadorComponent },
  data() {
    return {
      menu: [
        {
          title: "Fichas",
          prependIcon: "mdi-clipboard-list-outline",
          ruta: "/fichas",
        },
        { title: "Ejercicios", prependIcon: "mdi-run", ruta: "/ejercicios" },
        {
          title: "Equipamiento",
          prependIcon: "mdi-dumbbell",
          ruta: "/equipamientos",
        },
        { title: "Usuario", prependIcon: "mdi-account", ruta: "/usuario" },
      ],
      aplicacion: {
        nombre: "Centauri",
        icono: "/centauri_verde.png",
      },
    };
  },
  computed: {
    ...mapState(useUsuariosStore, ["token", "isLogged"]),
    menuItems() {
      if (this.isLogged) {
        return this.menu;
      } else {
        let menuSinRegistro = this.menu.filter(
          (item) => item.title !== "Usuario"
        );
        menuSinRegistro.push({
          title: "Registro",
          prependIcon: "mdi-account",
          ruta: "/login",
        });
        return menuSinRegistro;
      }
    },
  },
  methods: {
    ...mapActions(useUsuariosStore, ["peticionLogin"]),
    mostrarNavegador() {
      this.$refs.navegadorComponent.mostrarNavegador();
    },
  },
  async created() {},
};
</script>
