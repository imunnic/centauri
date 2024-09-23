import { defineStore } from "pinia";
import UsuariosService from "../services/usuarioService.js";

export const useUsuariosStore = defineStore("usuarios", {
  state: () => ({
    username: "",
    token: "",
    perfil: "",
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
        this.href = "http://localhost:8080/usuarios/" + usuario.id
        console.log(usuario);
        // this.href = await this.usuarioService.getHref();
        this.isLogged = true;
    },
  },
});
