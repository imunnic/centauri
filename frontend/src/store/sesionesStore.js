import { defineStore } from "pinia";
import SesionService from "../services/sesionService.js";
import { useUsuariosStore } from "./usuariosStore.js";

export const useSesionesStore = defineStore("sesiones", {
  state: () => ({
    sesionesRegistradas: [],
    sesionesService: new SesionService(),
  }),
  actions: {
    arrancarServicioSesion(token) {
      this.sesionesService.actualizarCabecera(token);
    },
    async cargarSesiones() {
      let response = await this.sesionesService.getAll();
    },
    async crearSesion(sesion){
      await this.sesionesService.crearSesion(sesion);

    }
  },
});
