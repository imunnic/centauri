<template>
  <v-container>
    <v-card>
      <v-card-title class="headline">Gestión de Usuario</v-card-title>
      <v-card-text>
        <v-form
          ref="formularioNombreUsuario"
          v-model="formularioNombreUsuarioValido"
          lazy-validation
        >
          <v-text-field
            v-model="nombreUsuario"
            label="Nombre de Usuario"
            :rules="reglasNombreUsuario"
            required
          ></v-text-field>
          <v-btn
            :disabled="!formularioNombreUsuarioValido"
            class="claro"
            @click="confirmarAccion('nombreUsuario')"
          >
            Cambiar Nombre de Usuario
          </v-btn>
        </v-form>

        <v-form
          ref="formularioPassword"
          v-model="formularioPasswordValido"
          lazy-validation
        >
          <v-text-field
            v-model="passwordActual"
            label="Contraseña Actual"
            type="password"
            :rules="reglasPassword"
            required
          ></v-text-field>
          <v-text-field
            v-model="nuevaPassword"
            label="Nueva Contraseña"
            type="password"
            :rules="reglasPassword"
            required
          ></v-text-field>
          <v-text-field
            v-model="confirmarNuevaPassword"
            label="Confirmar Nueva Contraseña"
            type="password"
            :rules="reglasConfirmarPassword"
            required
          ></v-text-field>
          <v-btn
            :disabled="!formularioPasswordValido"
            class="claro"
            @click="confirmarAccion('password')"
          >
            Cambiar Contraseña
          </v-btn>
        </v-form>
      </v-card-text>
    </v-card>

    <v-dialog v-model="dialogoConfirmacion" max-width="400">
      <v-card>
        <v-card-title class="headline">Confirmación</v-card-title>
        <v-card-text>
          ¿Estás seguro de que deseas cambiar tu
          {{
            accionAConfirmar === "nombreUsuario"
              ? "nombre de usuario"
              : "password"
          }}?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="rechazo" text @click="cancelarAccion">Cancelar</v-btn>
          <v-btn class="claro" text @click="ejecutarAccion">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { useAlertasStore } from "@/store/alertasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapActions, mapState } from "pinia";
export default {
  computed: {
    ...mapState(useUsuariosStore, ["username", "href"]),
  },
  data() {
    return {
      nombreUsuario: "",
      passwordActual: "",
      nuevaPassword: "",
      confirmarNuevaPassword: "",
      formularioNombreUsuarioValido: false,
      formularioPasswordValido: false,
      dialogoConfirmacion: false,
      accionAConfirmar: null,
      reglasNombreUsuario: [
        (v) => !!v || "El nombre de usuario es obligatorio",
        (v) => v.length >= 3 || "Debe tener al menos 3 caracteres",
        (v) => this.validarNombreUsuario(v),
      ],
      reglasPassword: [
        (v) => !!v || "El password es obligatorio",
        (v) => v.length >= 6 || "Debe tener al menos 6 caracteres",
      ],
      reglasConfirmarPassword: [
        (v) => !!v || "La confirmación de password es obligatoria",
        (v) => v === this.nuevaPassword || "Los passwords deben coincidir",
      ],
    };
  },
  methods: {
    ...mapActions(useUsuariosStore, [
      "existeUsuario",
      "cambiarNombre",
      "cambiarPassword",
    ]),
    ...mapActions(useAlertasStore, ["mostrarAlerta"]),
    async validarNombreUsuario(nombreUsuario) {
      if (!nombreUsuario) return true;
      if (nombreUsuario == this.username) return true;
      try {
        const existe = await this.existeUsuario(nombreUsuario);
        this.nombreUsuarioError = existe
          ? "El nombre de usuario ya está en uso"
          : "";
        return !existe || this.nombreUsuarioError;
      } catch (error) {
        console.error("Error al verificar nombre de usuario:", error);
        this.nombreUsuarioError = "Error al verificar nombre de usuario";
        return false;
      }
    },
    confirmarAccion(accion) {
      this.accionAConfirmar = accion;
      this.dialogoConfirmacion = true;
    },
    cancelarAccion() {
      this.dialogoConfirmacion = false;
      this.accionAConfirmar = null;
    },
    async ejecutarAccion() {
      this.dialogoConfirmacion = false;
      if (this.accionAConfirmar === "nombreUsuario") {
        await this.actualizarNombreUsuario();
      } else if (this.accionAConfirmar === "password") {
        await this.actualizarPassword();
      }
      this.accionAConfirmar = null;
    },
    async actualizarNombreUsuario() {
      try {
        await this.cambiarNombre(this.nombreUsuario);
        this.mostrarAlerta("Nombre cambiado con éxito", "success");
      } catch (error) {
        this.mostrarAlerta("No se ha podido cambiar el nombre", "error");
      }
    },
    async actualizarPassword() {
      if (this.nuevaPassword !== this.confirmarNuevaPassword) {
        return;
      }
      try {
        let passwords = {
          username: this.username,
          passwordAntigua: this.passwordActual,
          passwordNueva: this.nuevaPassword,
        };
        await this.cambiarPassword(passwords);
        this.passwordActual = "";
        this.nuevaPassword = "";
        this.confirmarNuevaPassword = "";
        this.mostrarAlerta("El password se ha cambiado con éxito", "success");
      } catch (error) {
        console.log(error);
        this.mostrarAlerta("El password no se ha podido cambiar", "error");
      }
    },
  },
  mounted() {
    this.nombreUsuario = this.username;
  },
};
</script>

<style scoped>
.v-card {
  max-width: 500px;
  margin: auto;
}
.v-btn {
  margin-top: 20px;
}
.v-form {
  margin: 10px;
}
</style>
