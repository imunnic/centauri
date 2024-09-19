import { defineStore } from "pinia";
import UsuariosService from "../services/usuarioService.js";

export const useUsuariosStore = defineStore("usuarios", {
  state: () => ({
    username: "",
    token: "",
    perfil: "",
    isLogged: false,
    primerInicio: null,
    usuarioService: new UsuariosService(),
  }),

  actions: {
    async peticionLogin() {
        this.token = await this.usuarioService.getToken();
        this.perfil = await this.usuarioService.getRol();
        this.username = await this.usuarioService.getUsername();
    },
  },
});
