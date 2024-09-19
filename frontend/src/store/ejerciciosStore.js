import { defineStore } from "pinia";
import EjercicioService from '@/services/ejercicioService.js';
import ejercicios from "../assets/ejercicios.json";

export const useEjerciciosStore = defineStore("ejercicios", {
  state: () => ({
    ejerciciosRegistrados: ejercicios,
    ejerciciosService: null,
  }),
  actions: {
    async cargarEjercicios() {
      let response = await this.ejerciciosService.getAll();
      this.ejerciciosRegistrados = response.data._embedded.ejercicios;
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
      ejercicio.numero_ejecutantes = 0;
      ejercicio.tipo_movimiento = "";
      ejercicio.tipo_contracción = "";
      ejercicio.velocidad = "";
      ejercicio.equipo = 0;
      ejercicio.musculos_principales = [];
      ejercicio.musculos_secundarios = [];
      ejercicio.url = "";
    },
    async modificarEjercicio(ejercicio) {
      await this.ejerciciosService.modificarEjercicio(ejercicio);
    },
    async eliminarEjercicio(ejercicio) {
      await this.ejerciciosService.eliminarEjercicio(ejercicio._links.self.href);
    }
  }
});
