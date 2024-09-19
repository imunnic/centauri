import { defineStore } from "pinia";
import fichas from "../assets/fichas.json"

export const useFichasStore = defineStore('fichas', {
  state: () => ({
    fichasRegistradas: fichas,
    fichasAprobadas: fichas.filter(e => e.estado == "APROBADO"),
    fichasPendientes: fichas.filter(e => e.estado == "PENDIENTE")
  }),
  actions: {

  }
})
