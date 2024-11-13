import PlanesService from '@/services/planesServices';
import { defineStore } from 'pinia';

export const usePlanesStore = defineStore('planes', {
  state: () => ({
    planesService:new PlanesService()
  }),
  actions: {
    arrancarServicioPlanes(token){
        this.planesService.actualizarCabecera(token);
    },
   async crearPlan(plan){
    await this.planesService.crearPlan(plan);
   }
  },
});
