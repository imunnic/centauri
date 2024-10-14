import { defineStore } from "pinia";
import GrupoService from "../services/grupoService.js";

export const useGruposStore = defineStore("grupos", {
  state: () => ({
    gruposRegistrados: [],
    gruposEncargado: [],
    solicitudesAcceso: [],
    grupoService: new GrupoService(),
  }),
  actions: {
    arrancarServicioGrupos(token) {
      this.grupoService.actualizarCabecera(token);
    },
    async crearGrupo(grupo) {
      await this.grupoService.postGrupo(grupo);
    },
    async getGruposEncargado(usuariohref){
      let response = await this.grupoService.getGruposPorEncargado(usuariohref);
      this.gruposEncargado = response.data._embedded.grupos;
    },
    async getSolicitudes(){
        let solicitudesTotales = [];
        for (let grupo of this.gruposEncargado) {
          let hrefGrupo = grupo._links.self.href;
          try {
            let response = await this.grupoService.getSolicitudesGrupo(hrefGrupo);
            let solicitudes = response.data._embedded.solicitudAccesoGrupos;
            solicitudesTotales.push(...solicitudes);
          } catch (error) {
            console.error(`Error obteniendo solicitudes del grupo ${hrefGrupo}:`, error);
          }
        }
        this.solicitudesAcceso = solicitudesTotales
        console.log(this.solicitudesAcceso);
    },
    async aceptarSolicitud(hrefSolicitud){
      let idSolicitud = hrefSolicitud.split('/').pop()
      let solicitud = {id: idSolicitud};
      await this.grupoService.aceptarSolicitud(solicitud);
    },
    async rechazarSolicitud(hrefSolicitud){
      let idSolicitud = hrefSolicitud.split('/').pop()
      let solicitud = {id: idSolicitud};
      await this.grupoService.rechazarSolicitud(solicitud);
    }
  },
});
