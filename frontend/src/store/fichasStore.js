import { defineStore } from "pinia";
import FichaService from "../services/fichaService.js";
import { useUsuariosStore } from "./usuariosStore.js";

export const useFichasStore = defineStore("fichas", {
  state: () => ({
    fichasRegistradas: [],
    fichaService: new FichaService(),
  }),
  actions: {
    arrancarServicioFicha(token) {
      this.fichaService.actualizarCabecera(token);
    },
    async aprobarFicha(ficha, estado) {
      await this.fichaService.cambiarEstado(ficha, estado);
    },
    async getFichaPorHref(href){
      href = href.split("/").pop();
      return this.fichasRegistradas.find(ficha => ficha.id==href);
    },
    async cargarFichas() {
      try {
        let response = await this.fichaService.getAprobadas();
        this.fichasRegistradas = response.data;
      } catch (error) {
        this.fichasRegistradas = [];
      }
    },
    async cargarPendientes() {
      try {
        let response = await this.fichaService.getPendientes();
        this.fichasRegistradas = response.data;
      } catch (error) {
        this.fichasRegistradas = [];
      }
    },
    async cargarPropias() {
      try {
        let response = await this.fichaService.getPropias(useUsuariosStore().id);
        this.fichasRegistradas = response.data._embedded.fichas;
      } catch (error) {
        this.fichasRegistradas = [];
      }
    },
    async cargarFichaDetalle(id) {
      return await this.fichaService.getFicha(id);
    },
    async cambiarEstado(href, estadoNuevo) {
      this.fichaService.cambiarEstado(href, estadoNuevo);
    },
    async grabarFicha(ficha) {
      ficha.estado = "PENDIENTE";
      ficha.autor = useUsuariosStore().href;
      await this.fichaService.postFicha(ficha);
    },
    async editarFicha(ficha, id) {
      ficha.estado = "PENDIENTE";
      await this.fichaService.editarFicha(ficha, id);
    },
    async borrarFicha(fichaId) {
      await this.fichaService.deleteFicha(fichaId);
    },
  },
});
