<!--
  Este componente es la cabecera de una aplicación. Está apoyado en la librería de Vuetify (https://vuetifyjs.com/). 
  Contiene un icono de menú para abrir el navegador, el nombre de la aplicación y un icono 
  para mostrar.
  
  Props:
  - aplicacion: Objeto que contiene el nombre y la ruta del icono de la aplicación desde la 
  carpeta raíz. Ej: {nombre:'mi nombre', icono:'/src/assets/favicon.png'}
  
  Para controlar el navegador se puede utilizar este código en el componente padre al 
  emitir el evento navegación:

  mostrarNavegador() {
      this.$refs.navegadorComponent.mostrarNavegador();
    }
  
  refiriendo de esta forma al componente con el que se navegue
-->

<template>
  <div>
    <v-app-bar :elevation="2" class="fondo d-flex align-center">
      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="$emit('navegacion')"></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title class="mr-3">
        {{ aplicacion.nombre }}
      </v-app-bar-title>

      <!-- Imagen redonda -->
      <v-avatar size="60" class="ml-auto icono">
        <img :src="aplicacion.icono" :alt="'Logo de la aplicación ' + aplicacion.nombre" 
        class="avatar-img" />
      </v-avatar>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  props: {
    aplicacion: {
      type: Object,
      required: true
    }
  },
  data() {
    return {};
  },
};
</script>

<style>
.fondo {
  background-color: #fff;
  /* Cambia el color de fondo si es necesario */
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* Asegura que la imagen se ajuste sin distorsionar */
}

.icono {
  margin-right: 10px;
}
</style>
