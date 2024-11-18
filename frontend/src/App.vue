<template>
  <v-app>
    <v-main>
      <MensajeAlertaComponent></MensajeAlertaComponent>
      <HeaderComponent
        @navegacion="mostrarNavegador"
        @intento-login="intentarLogin"
        @cerrar-sesion="cerrarSesion"
        @recuperar-password="recuperarPassword"
        :loggeado="isLogged"
        :usuarioNombre="username"
        :aplicacion="aplicacion"
        :key="intentosLogin"
      >
        <template v-slot:default>
          <v-menu offset-y :close-on-content-click="false">
            <template v-slot:activator="{ props }">
              <v-btn class="mr-2 claro" v-bind="props" @click="crearInvitacion">
                Crear Invitacion
              </v-btn>
            </template>

            <v-card>
              <v-card-text>
                <v-text-field
                  class="enlace"
                  label="Enlace"
                  append-icon="mdi-content-copy"
                  @click:append="copiarPortapapeles"
                  v-model="enlace"
                  enabled="false"
                >
                </v-text-field>
              </v-card-text>
            </v-card>
          </v-menu>
        </template>
      </HeaderComponent>
      <NavegadorComponent
        ref="navegadorComponent"
        :menu-items-prop="menuItems"
      ></NavegadorComponent>
    </v-main>
  </v-app>
</template>

<script>
import HeaderComponent from "./components/comun/HeaderComponent.vue";
import NavegadorComponent from "./components/comun/NavegadorComponent.vue";
import MensajeAlertaComponent from "@/components/comun/MensajeAlertaComponent.vue";
import { useUsuariosStore } from "./store/usuariosStore.js";
import { useAlertasStore } from "@/store/alertasStore.js";
import { mapActions, mapState } from "pinia";
import InvitacionesService from "@/services/invitacionService.js";
import config from "@/configuracion.json";
export default {
  components: { HeaderComponent, NavegadorComponent, MensajeAlertaComponent },
  data() {
    return {
      menu: [
        {
          title: "Fichas",
          prependIcon: "mdi-clipboard-list-outline",
          ruta: "/fichas",
        },
        { title: "Ejercicios", prependIcon: "mdi-run", ruta: "/ejercicios" },
        {
          title: "Equipamiento",
          prependIcon: "mdi-dumbbell",
          ruta: "/equipamientos",
        },
        {
          title: "Planes",
          prependIcon: "mdi-map-marker-path",
          ruta: "/planes",
        },
      ],
      aplicacion: {
        nombre: "Centauri",
        icono: "/centauri_verde.png",
      },
      intentosLogin: 0,
      enlace: "",
    };
  },
  computed: {
    ...mapState(useUsuariosStore, ["token", "isLogged", "username", "perfil"]),
    menuItems() {
      if (this.isLogged) {
        this.menu.push({
          title: "Grupos",
          prependIcon: "mdi-account-group",
          ruta: "/grupos",
        });
        this.menu.push({
          title: "Planificación",
          prependIcon: "mdi-calendar-outline",
          ruta: "/planificacion",
        });
        this.menu.push({
          title: "Usuario",
          prependIcon: "mdi-account",
          ruta: "/usuario",
        });
        if (this.perfil == "ECEF") {
          this.menu.push({
            title: "Gestión Usuarios",
            prependIcon: "mdi-cogs",
            ruta: "/gestion",
          });
        }
      }
      return this.menu;
    },
  },
  methods: {
    ...mapActions(useUsuariosStore, ["peticionLogin", "resetPassword"]),
    ...mapActions(useAlertasStore, [
      "mostrarAlerta",
      "mostrarError",
      "mostrarExito",
    ]),
    mostrarNavegador() {
      this.$refs.navegadorComponent.mostrarNavegador();
    },
    async intentarLogin(logeo) {
      logeo.username = logeo.usuario;
      try {
        await this.peticionLogin(logeo);
        this.$router.push("/planificacion");
        this.mostrarExito("Acceso correcto")
      } catch (error) {
        this.mostrarError("No ha podido realizarse el acceso");
      }
      this.intentosLogin++;
    },
    async crearInvitacion() {
      let servicioInvitaciones = new InvitacionesService();
      servicioInvitaciones.actualizarCabecera(this.token);
      try {
        let respuesta = await servicioInvitaciones.crearInvitacion();
        respuesta = respuesta.data._links.self.href.split("/").pop();
        respuesta = config.urlInvitaciones + respuesta;
        this.enlace = respuesta;
      } catch (error) {
        this.enlace = "No se ha podido crear el enlace";
        this.mostrarAlerta("No se ha podido crear un enlace", "error");
      }
    },
    async copiarPortapapeles() {
      try {
        await navigator.clipboard.writeText(this.enlace);
        this.mostrarAlerta("Texto copiado en el portapapeles", "success");
      } catch (error) {
        this.mostrarAlerta("No se ha podido copiar el texto", "error");
      }
    },
    cerrarSesion() {
      location.reload();
    },
    async recuperarPassword(correo) {
      try {
        await this.resetPassword(correo);
        this.mostrarAlerta("Contraseña actualizada", "success");
      } catch (error) {
        this.mostrarAlerta("No se ha podido actualizar la contraseña", "error");
      }
    },
  },
  async created() {},
};
</script>

<style scoped>
.enlace {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
