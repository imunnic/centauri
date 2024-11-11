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
            :rules="reglasNombre"
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
          ref="formularioCorreo"
          v-model="formularioCorreoValido"
          lazy-validation
        >
          <v-text-field
            v-model="correo"
            label="Correo de usuario"
            :rules="reglasCorreo"
            required
          ></v-text-field>
          <v-btn
            :disabled="!formularioCorreoValido"
            class="claro"
            @click="confirmarAccion('correoUsuario')"
          >
            Cambiar Correo
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
          ¿Estás seguro de que desea cambiar su
          {{
            accionAConfirmar === "nombreUsuario"
              ? "nombre de usuario"
              : accionAConfirmar === "correoUsuario"
              ? "correo electrónico"
              : "contraseña"
          }}?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="rechazo" text @click="cancelarCambio">Cancelar</v-btn>
          <v-btn class="claro" text @click="ejecutarCambio">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { mapActions, mapState } from "pinia";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useAlertasStore } from "@/store/alertasStore.js";

export default {
  data() {
    return {
      nombreUsuario: "",
      passwordActual: "",
      nuevaPassword: "",
      confirmarNuevaPassword: "",
      formularioNombreUsuarioValido: false,
      correo: "",
      formularioCorreoValido: false,
      formularioPasswordValido: false,
      dialogoConfirmacion: false,
      accionAConfirmar: null,
      reglasNombre: [
        (v) => !!v || "El nombre de usuario es requerido",
        async (v) => {
          if (!v) return true;
          let esValido;
          try {
            esValido = await this.validarNombreUsuario(v);
          } catch (error) {
            
          }
          return esValido || "El nombre de usuario ya existe";
        },
      ],
      reglasCorreo: [
        (v) => !!v || "El correo electrónico es requerido",
        (v) => /.+@.+\..+/.test(v) || "Debe ser un correo electrónico válido",
        async (v) => {
          if (!v) return true;
          let esValido;
          try {
            esValido = await this.validarCorreo(v);
          } catch (error) {
            
          }
          return esValido || "El correo ya existe";
        },
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
  computed: {
    ...mapState(useUsuariosStore, ["username"]),
  },
  methods: {
    ...mapActions(useUsuariosStore, [
      "existeUsuario",
      "cambiarNombre",
      "cambiarPassword",
      "comprobarCorreo",
      "modificarCorreo",
    ]),
    ...mapActions(useAlertasStore, ["mostrarAlerta"]),
    async validarNombreUsuario(nombreUsuario) {
      let existe = false;
      if (nombreUsuario == this.username) {
        existe = true;
      }
      try {
        existe = await this.existeUsuario(nombreUsuario);
        existe = !existe;
      } catch {
        this.mostrarAlerta("Error al comprobar los usuarios existentes","error");
      }
      return existe;
    },
    async validarCorreo(correo) {
      let existe = false;
      try {
        let response = await this.comprobarCorreo(correo);
        existe = response.data;
      } catch {
        this.mostrarAlerta("Error al comprobar los usuarios existentes","error");
      }
      return !existe;
    },
    confirmarAccion(accion) {
      this.accionAConfirmar = accion;
      this.dialogoConfirmacion = true;
    },
    cancelarCambio() {
      this.dialogoConfirmacion = false;
      this.accionAConfirmar = null;
    },
    async ejecutarCambio() {
      this.dialogoConfirmacion = false;
      if (this.accionAConfirmar === "nombreUsuario") {
        await this.actualizarNombreUsuario();
      } else if (this.accionAConfirmar === "password") {
        await this.actualizarPassword();
      } else if (this.accionAConfirmar === "correoUsuario"){
        await this.actualizarCorreo();
      }
      this.accionAConfirmar = null;
    },
    async actualizarNombreUsuario() {
      try {
        await this.cambiarNombre(this.nombreUsuario);
        this.mostrarAlerta(
          "Nombre cambiado con éxito, salga y vuelva a entrar para " +
            "cambiar contraseña",
          "success"
        );
      } catch (error) {
        this.mostrarAlerta("No se ha podido cambiar el nombre", "error");
      }
    },
    async actualizarCorreo() {
      try {
        let response = await this.modificarCorreo(this.correo);
        if(response){
          this.mostrarAlerta("Correo cambiado con éxito", "success");
        } else {
          this.mostrarAlerta("No se ha podido cambiar el correo", "error");
        }
      } catch(error) {
        console.log(error);
        this.mostrarAlerta("No se ha podido cambiar el correo", "error");
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
