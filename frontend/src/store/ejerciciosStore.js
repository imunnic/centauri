import { defineStore } from 'pinia';
import EjercicioService from '@/services/ejercicioService.js';

export const useEjerciciosStore = defineStore("ejercicios", {
  state: () => ({
    ejerciciosRegistrados: [],
    ejerciciosService: new EjercicioService(),
    ejercicioVacio:{}
  }),
  actions: {
    async cargarEjercicios() {
      try {
        let response = await this.ejerciciosService.getAll();
        this.ejerciciosRegistrados = response;
      } catch (error) {
        this.ejerciciosRegistrados = [];
      }
    },
    arrancarServicioEjercicios(token) {
      this.ejerciciosService = new EjercicioService();
      this.ejerciciosService.actualizarCabecera(token);
    },
    async agregarEjercicio(ejercicio) {
      await this.ejerciciosService.crearEjercicio(ejercicio);
    },
    resetEjercicio(ejercicio) {
      ejercicio.nombre = "";
      ejercicio.descripcion = "";
      ejercicio.cualidad = "";
      ejercicio.numeroEjecutantes = 1;
      ejercicio.tipoMovimiento = "";
      ejercicio.tipoContracción = "";
      ejercicio.velocidad = "";
      ejercicio.equipamiento = '';
      ejercicio.musculosPrincipales = [];
      ejercicio.musculosSecundarios = [];
      ejercicio.url = "";
      ejercicio.tipoCarga = ""
    },
    async modificarEjercicio(ejercicio) {
      await this.ejerciciosService.modificarEjercicio(ejercicio);
    },
    async eliminarEjercicio(ejercicio) {
      await this.ejerciciosService.eliminarEjercicio(ejercicio._links.self.href);
    },
    async getEquipamientoDeEjercicio(ejercicio){
      let equipamiento = '';
      try {
        equipamiento = 
        await this.ejerciciosService.getEquipamientoDeEjercicio(ejercicio._links.equipamiento.href);
      } catch(error) {
      }
      return equipamiento;
    }
  }
});
