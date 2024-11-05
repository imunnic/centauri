import { defineStore } from 'pinia';

export const useAlertasStore = defineStore('alertas', {
  state: () => ({
    mostrar: false,
    mensaje: '',
    tipo: 'success',
  }),
  actions: {
    mostrarAlerta(mensaje, tipo = 'success') {
      this.mensaje = mensaje;
      this.tipo = tipo;
      this.mostrar = true;

      setTimeout(() => {
        this.mostrar = false;
      }, 3000);
    },
    cerrarAlerta() {
      this.mostrar = false;
    },
  },
});
