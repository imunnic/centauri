import { defineStore } from "pinia";
import FichaService from '../services/fichaService.js';

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
      let response = await this.fichaService.getAll();
      this.fichasRegistradas = response.data._embedded.fichas;
    },
    async cargarFichaDetalle(id){
      return await this.fichaService.getFicha(id) 
    },
    async cambiarEstado(href, estadoNuevo){
      this.fichaService.cambiarEstado(href, estadoNuevo);
    }

  }
});
