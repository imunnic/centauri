<!--
  Este componente forma la barra lateral de navegación y las migas de pan automáticas para la interfaz de la aplicación. Utiliza la librería Vuetify (https://vuetifyjs.com/) como base de diseño.

  Props:
  - menuItemsProp: Array de objetos que define los elementos del menú lateral. Cada objeto del array debe tener las siguientes propiedades:
    - `title`: El texto que se mostrará para el ítem del menú.
    - `prependIcon`: El ícono que se mostrará antes del texto (íconos de Material Design).
    - `ruta`: La ruta a la que se navega al hacer clic en el ítem del menú.
-->

<template>
  <v-navigation-drawer v-model="barraLateral" temporary app>
    <v-list>
      <v-list-item
        v-for="(item, index) in menuItemsProp"
        :key="index"
        :title="item.title"
        :prepend-icon="item.prependIcon"
        @click="navegarARuta(item.ruta)"
      ></v-list-item>
    </v-list>
  </v-navigation-drawer>
  <v-main class="contenidoPrincipal" :class="claseRelleno">
    <v-breadcrumbs v-if="!esRutaContador" :items="migasDePan">
      <template v-slot:divider>
        <v-icon>mdi-chevron-right</v-icon>
      </template>
      <template v-slot:item="{ item }">
        <a @click="navegarARuta(item.href)" class="migas-pan">
          {{ item.title }}
        </a>
      </template>
    </v-breadcrumbs>
    <router-view></router-view>
  </v-main>
</template>

<script>
export default {
  props: {
    menuItemsProp: {
      type: Array,
      required: true,
    },
  },
  computed: {
    migasDePan() {
      const rutas = this.$route.path.split("/").filter(Boolean);

      return rutas.map((parteRuta, index) => {
        const href = "/" + rutas.slice(0, index + 1).join("/");
        return {
          title: this.primeraAMayuscula(parteRuta),
          href,
          disabled: false,
        };
      });
    },
    esRutaContador() {
      return this.$route.path.startsWith("/contador");
    },
    claseRelleno() {
      return this.$route.path.startsWith("/contador") ? "completo" : "";
    },
  },
  data() {
    return {
      barraLateral: false,
    };
  },
  methods: {
    primeraAMayuscula(cadena) {
      return cadena.charAt(0).toUpperCase() + cadena.slice(1);
    },
    mostrarNavegador() {
      this.barraLateral = !this.barraLateral;
    },
    navegarARuta(ruta) {
      this.$router.push(ruta);
    },
  },
};
</script>

<style scoped>
.v-navigation-drawer {
  position: fixed;
  height: 100%;
}
.contenidoPrincipal {
  padding-top: 0px;
  min-height: 100%;
}
.migas-pan {
  cursor: pointer;
  color: #4f4e4e;
}
.completo {
  background-color: var(--claro) !important;
}
</style>
