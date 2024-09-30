import { defineStore } from "pinia";
import UsuariosService from "../services/usuarioService.js";
import configuracion from "@/configuracion.json";

export const useUsuariosStore = defineStore("usuarios", {
  state: () => ({
    username: "",
    token: "",
    perfil: "",
    id: "",
    href: "",
    isLogged: false,
    primerInicio: null,
    usuarioService: new UsuariosService(),
  }),

  actions: {
    async peticionLogin() {
      try {
        let usuario = await this.usuarioService.login();
        this.token = usuario.token;
        this.perfil = usuario.usuario.rol;
        this.username = usuario.usuario.nombre;
        this.id = usuario.usuario.id;
        this.href = configuracion.urlBase + "usuarios/" + usuario.id;
        this.isLogged = true;
      } catch (error) {
        console.log(error);
      }
    },
  },
});
