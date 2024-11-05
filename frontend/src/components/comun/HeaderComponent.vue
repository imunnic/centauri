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

      <v-menu offset-y :close-on-content-click="false">
        <template v-slot:activator="{ props }">
          <v-btn class="mr-2 rechazo" v-bind="props">
            {{ loggeado ? 'Usuario' : 'Acceso' }}
          </v-btn>
        </template>

        <v-card>
          <v-card-title>{{loggeado ? 'Usuario': 'Registro'}}</v-card-title>
          <v-card-text>
            <div v-if="loggeado" class="user-info">
              <h2>{{ usuarioNombre }}</h2>
              <slot></slot>
            </div>
            <v-form ref="form" v-model="valid" v-else>
              <v-text-field
                v-model="logeo.usuario"
                label="Nombre de usuario"
                :rules="[rules.required]"
                required
              ></v-text-field>
              <v-text-field
                v-model="logeo.password"
                label="Contraseña"
                :rules="[rules.required]"
                type="password"
                required
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn class="claro" @click="loggeado ? cerrarSesion() : inicioSesion()">
              {{ loggeado ? 'Cerrar Sesión' : 'ACCEDER' }}
            </v-btn>
          </v-card-actions>
        </v-card>
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
export default {
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
      default:''
    },
  },
  data() {
    return {
      logeo: {
        usuario: '',
        password:''
      },
      valid: false,
      rules: {
        required: value => !!value || 'Requerido.',
      },
    };
  },
  methods: {
    inicioSesion() {
      if (this.$refs.form.validate()) {
        this.$emit('intentoLogin', this.logeo)
      }
    },
    cerrarSesion() {
      this.$emit('cerrarSesion');
    }
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

.user-info {
  padding: 16px;
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