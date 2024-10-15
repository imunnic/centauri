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
      this.sesionesRealizadasRegistradas =
        response.data._embedded.sesionesRealizadas;
      this.sesionesRealizadasRegistradas.sort((a, b) => {
        let fechaA = new Date(a.fechaSesion);
        let fechaB = new Date(b.fechaSesion);
        return fechaB - fechaA;
      });
    },
    async getSesionesRealizadasDeUsuarioyGrupo(idUsuario, nombreGrupo) {
      let response =
        await this.sesionesRealizadasService.getSesionesRealizadasPorUsuarioYGrupo(
          idUsuario,
          nombreGrupo
        );
      let sesionesUsuario = response.data;
      sesionesUsuario.sort((a, b) => {
        let fechaA = new Date(a.fechaSesion);
        let fechaB = new Date(b.fechaSesion);
        return fechaB - fechaA;
      });
      return sesionesUsuario;
    },
    async crearSesionRealizada(sesionRealizada) {
      await this.sesionesRealizadasService.crearSesionRealizada(
        sesionRealizada
      );
    },
    async comprobarSesionRealizada(hrefUsuario, hrefSesion) {
      let response =
        await this.sesionesRealizadasService.getSesionesRealizadasPorUsuarioYSesion(
          hrefUsuario,
          hrefSesion
        );
      let sesionesRealizadas = response.data._embedded.sesionesRealizadas;
      if (sesionesRealizadas.length > 0) {
        return true;
      } else {
        return false;
      }
    },
  },
});
