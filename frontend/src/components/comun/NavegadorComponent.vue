<!--
  Este componente forma la barra lateral de navegación de forma que se pueda visualizar o no.
  Utiliza la librería de Vuetify(https://vuetifyjs.com/)como apoyo. Cuenta con migas de pan
  automáticas y las items del menú se personalizan pasandolos por propiedad.

  Props:
  - menuItemsProp: Array de objetos que define los elementos del menú lateral. Cada objeto debe 
    contener las propiedades `title` (texto del menú), `prependIcon` (ícono del menú) y `ruta` 
    (ruta a la que se debe navegar al hacer clic en el ítem).

  Data:
  - barraLateral: Booleano que controla la visibilidad del menú lateral.

  Computed:
  - migasDePan: Computa las migas de pan basadas en la ruta actual. Divide la ruta en partes y 
    genera enlaces (`href`) y títulos (`title`) para cada parte. La última parte está deshabilitada 
    para indicar que es la página actual.
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
  <v-main class="contenidoPrincipal">
    <v-breadcrumbs :items="migasDePan" class="v-container">
      <template v-slot:divider>
        <v-icon>mdi-chevron-right</v-icon>
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
      required: true
    }
  },
  computed: {
    migasDePan() {
      const rutas = this.$route.path.split('/').filter(Boolean);

      return rutas.map((parteRuta, index) => {
        const href = '/' + rutas.slice(0, index + 1).join('/'); 
        return {
          title: this.primeraAMayuscula(parteRuta),
          href,
          disabled: index === rutas.length - 1
        };
      });
    }
  },
  data() {
    return {
      barraLateral: false
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
    }
  }
}
</script>

<style scoped>
.v-navigation-drawer {
  position: fixed;
  height: 100%;
}
.contenidoPrincipal {
  padding-top: 0px;
}
</style>
