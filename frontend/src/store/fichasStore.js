import { defineStore } from "pinia";
import FichaService from '../services/fichaService.js';
import { useUsuariosStore } from "./usuariosStore.js";

export const useFichasStore = defineStore('fichas', {
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
    async cargarFichas() {
      let response = await this.fichaService.getAprobadas();
      this.fichasRegistradas = response.data;
    },
    async cargarPendientes(){
      let response = await this.fichaService.getPendientes();
      this.fichasRegistradas = response.data;
    },
    async cargarPropias(){
      let response = await this.fichaService.getPropias(useUsuariosStore().id);
      this.fichasRegistradas =response.data._embedded.fichas;
    },
    async cargarFichaDetalle(id){
      return await this.fichaService.getFicha(id) 
    },
    async cambiarEstado(href, estadoNuevo){
      this.fichaService.cambiarEstado(href, estadoNuevo);
    },
    async grabarFicha(ficha){
      ficha.estado = "PENDIENTE";
      ficha.autor = useUsuariosStore().href;
      await this.fichaService.postFicha(ficha);
    },
    async editarFicha(ficha, id){
      ficha.estado = "PENDIENTE";
      await this.fichaService.editarFicha(ficha, id)
    },
    async borrarFicha(href){
      await this.fichaService.deleteFicha(href);
    }

  }
});
