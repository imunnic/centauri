import { defineStore } from "pinia";
import GrupoService from "../services/grupoService.js";

export const useGruposStore = defineStore("grupos", {
  state: () => ({
    gruposRegistrados: [],
    gruposEncargado: [],
    grupoService: new GrupoService(),
  }),
  actions: {
    arrancarServicioGrupos(token) {
      this.grupoService.actualizarCabecera(token);
    },
    async crearGrupo(grupo) {
      await this.grupoService.postGrupo(grupo);
    },
  },
});
