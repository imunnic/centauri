import { defineStore } from 'pinia';
import EquipamientosService from "@/services/equipamientoService.js";

export const useEquipamientosStore = defineStore("equipamientos", {
  state: () => ({
    equipamientosRegistrados: [],
    equipamientosService: new EquipamientosService(),
  }),
  actions: {
    async cargarEquipamientos() {
      try{
        let response = await this.equipamientosService.getAll();
        this.equipamientosRegistrados = response;
      }
      catch(error){
        this.equipamientosRegistrados = [];
      }
    },
    arrancarServicioEquipamientos(token) {
      this.equipamientosService.actualizarCabecera(token);
    },
    async agregarEquipamiento(equipamiento) {
      await this.equipamientosService.crearEquipamiento(equipamiento);
    },
    async modificarEquipamiento(equipamiento) {
      await this.equipamientosService.modificarEquipamiento(equipamiento);
    },
    async eliminarEquipamiento(equipamiento) {
      await this.equipamientosService.eliminarEquipamiento(
        equipamiento._links.self.href
      );
    },
    resetEquipamiento(equipamiento) {
      equipamiento.nombre = "";
      equipamiento.descripcion = "";
      equipamiento.tipo = "";
      equipamiento.url = "";
    },
  },
});
