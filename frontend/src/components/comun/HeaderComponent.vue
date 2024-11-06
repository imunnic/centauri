<!--
  Este componente representa la cabecera de una aplicación, utilizando la librería Vuetify (https://vuetifyjs.com/). 
  Incluye un botón de menú para abrir el navegador, el nombre de la aplicación y un ícono de usuario.

  Props:
  - aplicacion: Objeto que contiene el nombre y la ruta del ícono de la aplicación desde la carpeta raíz. 
    Ejemplo: {nombre: 'mi nombre', icono: '/src/assets/favicon.png'}
  - loggeado: Booleano que indica si el usuario está autenticado.
  - usuarioNombre: Nombre del usuario autenticado. Por defecto, es una cadena vacía.

  Eventos:
  - navegacion: Se emite cuando el usuario hace clic en el botón del menú.
  - intentoLogin: Se emite con los datos de inicio de sesión cuando el usuario intenta acceder.
  - cerrarSesion: Se emite cuando el usuario cierra sesión.

  Para controlar el navegador desde el componente padre al emitir el evento 'navegacion', puedes usar el siguiente código:

  mostrarNavegador() {
      this.$refs.navegadorComponent.mostrarNavegador();
  }

  Asegúrate de referenciar correctamente el componente de navegación al que deseas acceder.
-->
<template>
  <div>
    <v-app-bar :elevation="2" class="fondo d-flex align-center">
      <template v-slot:prepend>
        <v-btn aria-label="menu" class="fondo" icon="mdi-menu" @click="$emit('navegacion')"></v-btn>
      </template>

      <v-app-bar-title class="mr-3">
        {{ aplicacion.nombre }}
      </v-app-bar-title>

      <v-menu v-model="mostrarMenu" offset-y :close-on-content-click="false">
        <template v-slot:activator="{ props }">
          <v-btn class="mr-2 rechazo" v-bind="props">
            {{ loggeado ? 'Usuario' : 'Acceso' }}
          </v-btn>
        </template>

        <LoginComponent
          :loggeado="loggeado"
          :usuarioNombre="usuarioNombre"
          @intentoLogin="intentoLogin"
          @cerrarSesion="cerrarSesion"
          @recuperar-password="recuperarPassword"
        >
        <slot></slot>
      </LoginComponent>
      </v-menu>
      
      <v-avatar size="60" class="ml-auto icono">
        <img
          :src="aplicacion.icono"
          :alt="'Logo de la aplicación ' + aplicacion.nombre"
          class="avatar-img"
        />
      </v-avatar>
    </v-app-bar>
  </div>
</template>

<script>
import LoginComponent from './LoginComponent.vue';

export default {
  components: {
    LoginComponent,
  },
  props: {
    aplicacion: {
      type: Object,
      required: true,
    },
    loggeado: {
      type: Boolean,
      required: true,
    },
    usuarioNombre: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      mostrarMenu:false
    }
  },
  methods: {
    intentoLogin(datosLogin) {
      this.$emit('intento-login', datosLogin);
    },
    cerrarSesion() {
      this.$emit('cerrar-sesion');
    },
    recuperarPassword(correo) {
      this.$emit('recuperar-password', correo);
      this.cerrarMenu();
    },
    cerrarMenu() {
      this.mostrarMenu = false;
    },
  },
};
</script>

<style scoped>
.fondo {
  background-color: #fff;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.icono {
  margin-right: 10px;
}

.v-card {
  min-width: 400px;
}

@media (max-width: 500px) {
  .v-card {
    min-width: 300px;
  }
}
</style>
