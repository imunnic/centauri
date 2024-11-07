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
    marcas:"",
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
      let response = await this.usuarioService.login(this.login);
      this.username = response.usuario.nombre;
      this.id = response.usuario.id;
      this.token = response.token;
      this.perfil = response.usuario.rol;
      this.marcas = response.usuario.marcas;
      this.href = configuracion.urlBase + "usuarios/" + this.id;
      this.isLogged = true;

      this.arrancarServicios();
    },

    async getUsuario(href){
      let response = await this.usuarioService.getUsuario(href);
      return response.data.nombre;
    },

    async existeUsuario(nombreUsuario){
      let respone = await this.usuarioService.existeUsuario(nombreUsuario);
      return respone.data;
    },

    async renovarToken(){
      let renovacion = await this.usuarioService.renovarToken();
      this.token = renovacion.data.token;
      this.arrancarServicios();
    },

    async cambiarNombre(nombre){
      await this.usuarioService.cambiarNombreUsuario(nombre);
    },

    async actualizarMarcas(nuevaMarca){
      if (typeof nuevaMarca === 'object') {
        for (let key in nuevaMarca) {
          if (typeof nuevaMarca[key] === 'string') {
            nuevaMarca[key] = parseInt(nuevaMarca[key], 10);
          }
        }
      }
      this.marcas = {
        ...this.marcas,
        ...nuevaMarca
      };
      let marcasUsuario = {
        href: this.href,
        marcas: {...nuevaMarca}
      }
      await this.usuarioService.actualizarMarcas(marcasUsuario);
    },

    async eliminarMarca(usuario){
      this.marcas = usuario.marcas;
      usuario.href = this.href;
      await this.usuarioService.borrarMarca(usuario);
    },

    encontrarMarca(nombreEjercicio){
      return this.marcas[nombreEjercicio] !== undefined ? this.marcas[nombreEjercicio] : 0;
    },

    async cambiarPassword(passwords){
      await this.usuarioService.cambiarPassword(passwords);
    },

    async resetPassword(correo){
      await this.usuarioService.recuperarPassword(correo);
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
