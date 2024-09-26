import { defineStore } from "pinia";
import EjercicioService from '@/services/ejercicioService.js';

export const useEjerciciosStore = defineStore("ejercicios", {
  state: () => ({
    ejerciciosRegistrados: [],
    ejerciciosService: new EjercicioService(),
    ejercicioVacio:{}
  }),
  actions: {
    async cargarEjercicios() {
      let response = await this.ejerciciosService.getAll();
      this.ejerciciosRegistrados = response;
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
      ejercicio.numeroEjecutantes = 0;
      ejercicio.tipoMovimiento = "";
      ejercicio.tipoContracción = "";
      ejercicio.velocidad = "";
      ejercicio.equipo = '';
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
    }
  }
});
