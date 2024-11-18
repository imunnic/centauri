<!--
  Este componente de tarjeta permite gestionar la autenticación de usuarios, mostrando diferentes vistas dependiendo del estado de autenticación y de la necesidad de recuperación de contraseña. Se utiliza Vuetify para la estructura de la tarjeta y sus elementos.

  Props:
  - `loggeado`: Booleano que indica si el usuario ya ha iniciado sesión.
  - `usuarioNombre`: String que muestra el nombre del usuario logueado.

  Slots:
  - Default: Permite contenido personalizado en la vista de usuario logueado.

  Events:
  - `intentoLogin`: Emitido con los datos de inicio de sesión al intentar acceder.
  - `cerrarSesion`: Emitido al cerrar la sesión.
  - `recuperar-password`: Emitido con el correo electrónico al iniciar la recuperación de contraseña.

-->


<template>
    <v-card>
      <v-card-title>{{ loggeado ? 'Usuario' : recuperar ? 'Recuperación de contraseña' : 'Registro' }}</v-card-title>
      <v-card-text>
        <div v-if="loggeado" class="user-info">
          <h2>{{ usuarioNombre }}</h2>
          <slot></slot>
        </div>
        <v-form ref="form" v-model="valid" v-else>
          <div v-if="!recuperar">
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
              :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
              :type="visible ? 'text' : 'password'"
              required
              @click:append-inner="visible = !visible"
            ></v-text-field>
              <a @click="activarRecuperacion">No recuerdo la contraseña</a>
          </div>
          <div v-else>
            <v-text-field
              v-model="recuperacionCorreo"
              label="Correo electrónico"
              :rules="[rules.required]"
              required
            ></v-text-field>
            <v-btn class="claro" @click="enviarCorreoRecuperacion">
              Enviar correo de recuperación
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn class="claro" @click="loggeado ? cerrarSesion() : inicioSesion()">
          {{ loggeado ? 'Cerrar Sesión' : 'ACCEDER' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </template>
  
  <script>
  export default {
    props: {
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
        logeo: {
          usuario: '',
          password: ''
        },
        valid: false,
        recuperar: false,
        recuperacionCorreo: '',
        rules: {
          required: value => !!value || 'Requerido.',
        },
        visible:false,
      };
    },
    methods: {
      inicioSesion() {
        if (this.$refs.form.validate()) {
          this.$emit('intentoLogin', this.logeo);
        }
      },
      cerrarSesion() {
        this.$emit('cerrarSesion');
      },
      activarRecuperacion() {
        this.recuperar = true;
      },
      enviarCorreoRecuperacion() {
        if (this.$refs.form.validate()) {
          this.$emit('recuperar-password', this.recuperacionCorreo);
        }
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
  a{
    display: block;
    width: 100%;
    text-align: end;
  }
  a:hover{
    cursor: pointer;
    text-decoration: underline;
    color: #0202aa;
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
  