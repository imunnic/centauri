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
    async cargarSesiones(grupos) {
      const sesionesConFichas = await Promise.all(
        grupos.map(async (grupo) => {
          let response = await this.sesionesService.getSesionesPorGrupo(
            grupo.nombre
          );
          let sesiones = response.data._embedded.sesiones;

          return Promise.all(
            sesiones.map(async (sesion) => {
              const hrefFichas = sesion._links.fichas.href;
              const fichasResponse =
                await this.sesionesService.getFichasDeSesion(hrefFichas);
              const fichas = fichasResponse.data._embedded.fichas;

              const fechaOriginal = new Date(sesion.fecha);
              const dia = String(fechaOriginal.getDate()).padStart(2, "0"); 
              const mes = String(fechaOriginal.getMonth() + 1).padStart(2, "0");
              const ano = fechaOriginal.getFullYear();
              const fechaFormateada = `${dia}/${mes}/${ano}`;

              return {
                grupo: grupo,
                fecha: fechaFormateada,
                nombre: sesion.nombre,
                fichas: fichas,
              };
            })
          );
        })
      );

      return sesionesConFichas.flat();
    },
    async crearSesion(sesion) {
      await this.sesionesService.crearSesion(sesion);
    },
  },
});
