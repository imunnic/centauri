import { defineStore } from "pinia";
import UsuariosService from "../services/usuarioService.js";

export const useUsuariosStore = defineStore("usuarios", {
  state: () => ({
    username: "",
    token: "",
    perfil: "",
    id:"",
    href:"",
    isLogged: false,
    primerInicio: null,
    usuarioService: new UsuariosService(),
  }),

  actions: {
    async peticionLogin() {
        this.token = await this.usuarioService.getToken();
        this.perfil = await this.usuarioService.getRol();
        let usuario = await this.usuarioService.getUsername();
        this.username = usuario.nombre;
        this.id = usuario.id;
        this.href = "http://localhost:8080/usuarios/" + usuario.id
        this.isLogged = true;
    },
  },
});
