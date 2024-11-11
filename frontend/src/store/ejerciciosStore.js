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
    async getEjerciciosPorCarga(carga){
      await this.cargarEjercicios();
      return this.ejerciciosRegistrados.filter(e => e.tipoCarga == carga);
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
    tipoCargaEjercicio(nombreEjercicio){
      let tipo = "";
      if(nombreEjercicio){
        let ejercicio = this.ejerciciosRegistrados.filter(e => e.nombre == nombreEjercicio)[0];
        if(ejercicio){
          tipo = ejercicio.tipoCarga;
        }
      }
      return tipo;
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
