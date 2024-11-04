<template>
  <v-app>
    <v-main>
      <MensajeAlertaComponent
      :mostrar="mostrarAlerta"
      :mensaje="mensajeAlerta"
      :tipo="tipoAlerta"
      @cerrar="mostrarAlerta = false"
    ></MensajeAlertaComponent>
      <HeaderComponent
        @navegacion="mostrarNavegador"
        @intentoLogin="intentarLogin"
        @cerrarSesion="cerrarSesion"
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
            append-icon="mdi-content-copy"
            @click:append="copiarPortapapeles"
            v-model="enlace"
            enabled="false">
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
        { title: "Usuario", prependIcon: "mdi-account", ruta: "/usuario" },
      ],
      aplicacion: {
        nombre: "Centauri",
        icono: "/centauri_verde.png",
      },
      intentosLogin:0,
      enlace:"",
      mostrarAlerta:false,
      mensajeAlerta:'',
      tipoAlerta:'success'
    };
  },
  computed: {
    ...mapState(useUsuariosStore, ["token", "isLogged", "username"]),
    menuItems() {
      if (this.isLogged) {
        let menuLogeado = this.menu.push({
          title: "Grupos",
          prependIcon: "mdi-account-group",
          ruta: "/grupos",
        })
        return this.menu;
      } else {
        let menuSinRegistro = this.menu.filter(
          (item) => item.title !== "Usuario"
        );
        return menuSinRegistro;
      }
    },
  },
  methods: {
    ...mapActions(useUsuariosStore, ["peticionLogin"]),
    mostrarNavegador() {
      this.$refs.navegadorComponent.mostrarNavegador();
    },
    async intentarLogin(logeo) {
      logeo.username = logeo.usuario;
      try {
        await this.peticionLogin(logeo);
        this.$router.push("/usuario")
      } catch (error) {
      }
      this.intentosLogin++;
    },
    async crearInvitacion(){
      let servicioInvitaciones = new InvitacionesService();
      servicioInvitaciones.actualizarCabecera(this.token);
      let respuesta = await servicioInvitaciones.crearInvitacion();
      respuesta = respuesta.data._links.self.href.split('/').pop();
      respuesta = config.urlBaseApp + respuesta;
      this.enlace = respuesta;
    },
    async copiarPortapapeles(){
      await navigator.clipboard.writeText(this.enlace);
      this.mostrarAlertaTemporal('Texto copiado en el portapapeles','success');
    },
    mostrarAlertaTemporal(mensaje, tipo) {
      this.mensajeAlerta = mensaje;
      this.tipoAlerta = tipo;
      this.mostrarAlerta = true;
    },
    cerrarSesion(){
      location.reload();
    }
  },
  async created() {},
};
</script>

<style scoped>
.enlace{
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
