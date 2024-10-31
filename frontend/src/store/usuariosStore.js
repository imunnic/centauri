import { defineStore } from "pinia";
import UsuariosService from "../services/usuarioService.js";
import configuracion from "@/configuracion.json";

import { useFichasStore } from "@/store/fichasStore.js";
import { useEjerciciosStore } from "@/store/ejerciciosStore.js";
import { useEquipamientosStore } from "@/store/equipamientosStore.js";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useGruposStore } from "@/store/gruposStore.js";

export const useUsuariosStore = defineStore("usuarios", {
  state: () => ({
    username: "",
    token: "",
    perfil: "",
    id: "",
    href: "",
    login:{},
    isLogged: false,
    primerInicio: null,
    usuarioService: new UsuariosService(),
  }),

  actions: {
    async peticionLogin(logeo){
      this.login = logeo
      let usuario = await this.usuarioService.login(this.login);
      this.username = usuario.usuario.nombre;
      this.id = usuario.usuario.id;
      this.token = usuario.token;
      this.perfil = usuario.usuario.rol;
      this.href = configuracion.urlBase + "usuarios/" + this.id;
      this.isLogged = true;

      this.arrancarServicios();
    },

    async getUsuario(href){
      let response = await this.usuarioService.getUsuario(href);
      return response.data.nombre;
    },

    async renovarToken(){
      let renovacion = await this.usuarioService.renovarToken();
      this.token = renovacion.data.token;
      this.arrancarServicios();
    },

    arrancarServicios(){
      const fichasStore = useFichasStore();
      const ejerciciosStore = useEjerciciosStore();
      const equipamientosStore = useEquipamientosStore();
      const sesionesStore = useSesionesStore();
      const sesionesRealizadasStore = useSesionesRealizadasStore();
      const gruposStore = useGruposStore();

      fichasStore.arrancarServicioFicha(this.token);
      ejerciciosStore.arrancarServicioEjercicios(this.token);
      equipamientosStore.arrancarServicioEquipamientos(this.token);
      sesionesStore.arrancarServicioSesion(this.token);
      sesionesRealizadasStore.arrancarServicioSesionRealizada(this.token);
      gruposStore.arrancarServicioGrupos(this.token);
      this.usuarioService.actualizarCabecera(this.token);
    }
  },
});
