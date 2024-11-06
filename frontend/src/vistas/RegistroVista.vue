<template>
  <div>
    <v-card class="margen">
      <v-card-title>
        Formulario de registro
      </v-card-title>
      <v-card-text>
        <v-container class="pa-4" max-width="400px">
          <v-form ref="formulario" v-model="esValido">
      <v-text-field
        label="Nombre de Usuario"
        v-model="usuario.nombre"
        :rules="reglasNombre"
        required
      ></v-text-field>

      <v-text-field
        label="Correo Electrónico"
        v-model="usuario.correo"
        :rules="reglasCorreo"
        required
      ></v-text-field>

      <v-text-field
        label="Contraseña"
        v-model="usuario.password"
        :rules="reglasPassword"
        :type="mostrarPassword ? 'text' : 'password'"
        required
        append-icon="mdi-eye"
        @click:append="mostrarPassword = !mostrarPassword"
      ></v-text-field>

      <v-text-field
        label="Confirmar Contraseña"
        v-model="confirmarPassword"
        :rules="reglasConfirmarPassword"
        :type="mostrarPassword ? 'text' : 'password'"
        required
        append-icon="mdi-eye"
        @click:append="mostrarPassword = !mostrarPassword"
      ></v-text-field>

      <v-btn :disabled="!esValido" class="claro" @click="enviarFormulario">
        Registrarse
      </v-btn>
    </v-form>
  </v-container>
      </v-card-text>
    </v-card>
  </div>
</template>
<script>
import UsuarioService from "@/services/usuarioService.js";
import {useAlertasStore} from "@/store/alertasStore.js";
import {useUsuariosStore} from "@/store/usuariosStore.js";
import {mapActions} from 'pinia';
import axios from 'axios';
import config from '@/configuracion.json';
export default {
  data() {
    return {
      esValido: false,
      usuario:{
        nombre: "",
      correo: "",
      password: "",
      invitacion: this.$route.params.id
      },
      confirmarPassword:"",
      mostrarPassword: false,
      reglasNombre: [
        (v) => !!v || "El nombre de usuario es requerido",
        (v) => v => this.validarNombreUsuario(v) || "El nombre de usuario ya existe",
      ],
      reglasCorreo: [
        (v) => !!v || "El correo electrónico es requerido",
        (v) => /.+@.+\..+/.test(v) || "Debe ser un correo electrónico válido",
      ],
      reglasPassword: [
        (v) => !!v || "La contraseña es requerida",
        (v) => v.length >= 6 || "Debe tener al menos 6 caracteres",
      ],
      reglasConfirmarPassword: [
        (v) => !!v || "La confirmación de la contraseña es requerida",
        (v) =>
          v === this.usuario.password || "Las contraseñas deben coincidir",
      ],
    }
  },
  methods: {
    ...mapActions(useAlertasStore,['mostrarAlerta']),
    ...mapActions(useUsuariosStore,['existeUsuario']),
    async validarNombreUsuario(nombreUsuario) {
      if (!nombreUsuario) return true;
      if (nombreUsuario == this.username) return true;
      try {
        const existe = await this.existeUsuario(nombreUsuario);
        this.nombreUsuarioError = existe ? "El nombre de usuario ya está en uso" : "";
        return !existe || this.nombreUsuarioError;
      } catch (error) {
        console.error("Error al verificar nombre de usuario:", error);
        this.nombreUsuarioError = "Error al verificar nombre de usuario";
        return false;
      }
    },
    async enviarFormulario() {
      if (this.$refs.formulario.validate()) {
        let servicioUsuario = new UsuarioService();
        await servicioUsuario.registroUsuario(this.usuario);
        this.mostrarAlerta('Registro realizado, ya puede acceder', 'success')
        this.$router.push("/fichas");
      }
    },
  },
  async created() {
    let url = config.urlBase + "invitaciones/" + this.$route.params.id;
    try {
      await axios.get(url);
    } catch (error) {
      this.$router.push("/fichas");
    }
  },
}
</script>
<style scoped>
.margen{
 margin:10px;
}
</style>
