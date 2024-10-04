<!--
  Este componente es un botón flotante que se apoya en la librería Vuetify 
  (https://vuetifyjs.com/). Está diseñado para estar en la esquina inferior derecha de la 
  pantalla y puede ser utilizado para realizar acciones destacadas.

  Props:
  - icon: Define el icono que se mostrará dentro del botón. Es una cadena que debe coincidir con 
    los nombres de los iconos de Material Design Icons (por defecto, usa 'mdi-plus').
  - clasesAdicionales: Permite agregar clases adicionales al botón para personalizar su estilo. 
    Por defecto, se aplica la clase 'claro'.

-->

<template>
  <v-btn
    icon
    class="fab-button"
    @click="$emit('click')"
    :class="clasesAdicionales"
    :id="nombre"
  >
    <v-icon>{{ icon }}</v-icon>
  </v-btn>
</template>

<script>
export default {
  props: {
    nombre: {
      type: String,
      default: "crear"
    },
    icon: {
      type: String,
      default: "mdi-plus",
    },
    clasesAdicionales: {
      type: String,
      default: "claro",
    },
  },
};
</script>

<style scoped>
.fab-button {
  position: fixed;
  right: 16px;
  bottom: 16px;
}
</style>
