import { defineStore } from "pinia";
import SesionesRealizadaService from "../services/sesionRealizadaService.js";

export const useSesionesRealizadasStore = defineStore("sesionesRealizadas", {
  state: () => ({
    sesionesRealizadasRegistradas: [],
    sesionesRealizadasService: new SesionesRealizadaService(),
  }),
  actions: {
    arrancarServicioSesionRealizada(token) {
      this.sesionesRealizadasService.actualizarCabecera(token);
    },
    async cargarSesionesRealizadas(hrefUsuario) {
      let response =
        await this.sesionesRealizadasService.getSesionesRealizadasPorUsuario(
          hrefUsuario
        );
        this.sesionesRealizadasRegistradas = response.data._embedded.sesionesRealizadas;
    },
    async crearSesionRealizada(sesionRealizada) {
      await this.sesionesRealizadasService.crearSesionRealizada(
        sesionRealizada
      );
    },
    async comprobarSesionRealizada(hrefUsuario, hrefSesion){
      let response = await this.sesionesRealizadasService.getSesionesRealizadasPorUsuarioYSesion(hrefUsuario,hrefSesion);
      let sesionesRealizadas =  response.data._embedded.sesionesRealizadas;
      if (sesionesRealizadas.length > 0) {
        return true;
      } else {
        return false;
      }
    }
  },
});
