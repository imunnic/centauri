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
   },
   async getPlanes(){
    let response = await this.planesService.getPlanes();
    return response.data;
   },
   async getPlan(id){
    let response = await this.planesService.getPlan(id);
    return response.data;
   },
   async agregarSesiones(agregarSesionesRequest){
    await this.planesService.agregarSesiones(agregarSesionesRequest);
   },
   async borrarPlan(planId){
    await this.planesService.eliminarPlan(planId);
   },
   async modificarPlan(plan){
    await this.planesService.modificarPlan(plan);
   }
  },
});
