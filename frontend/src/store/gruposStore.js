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
    async getGruposSinUsuario(usuarioId){
      return await this.grupoService.getGruposSinMiembro(usuarioId);
    },
    async getGruposUsuario(usuarioHref){
      let response = await this.grupoService.getGruposPorMiembro(usuarioHref);
      this.gruposRegistrados = response.data._embedded.grupos;
    },
    async getGruposEncargado(usuarioHref){
      let response = await this.grupoService.getGruposPorEncargado(usuarioHref);
      this.gruposEncargado = response.data._embedded.grupos;
    },
    async existeGrupo(nombre){
      let response = await this.grupoService.existsGrupoByNombre(nombre)
      return response.data;
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
    async realizarSolicitud(solicitud){
      await this.grupoService.postSolicitudGrupo(solicitud);
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
