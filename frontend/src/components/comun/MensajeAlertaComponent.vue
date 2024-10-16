<!--
  Este componente `MensajeAlerta` está diseñado para mostrar alertas temporales utilizando el sistema de Vuetify (https://vuetifyjs.com/). Permite mostrar mensajes de diferentes tipos (éxito, error, advertencia, etc.) de manera visualmente atractiva y con transiciones suaves.

  Props:
  - mostrar: Booleano que indica si la alerta debe ser visible. Este prop es obligatorio.
  - mensaje: String que contiene el texto que se mostrará en la alerta. Este prop es obligatorio.
  - tipo: String que define el tipo de alerta (por ejemplo, 'success', 'error'). El valor predeterminado es 'success'.
  - temporal: Booleano que determina si la alerta debe cerrarse automáticamente después de un tiempo determinado. El valor predeterminado es `true`.
  - tiempo: Número que especifica el tiempo en milisegundos antes de que la alerta se cierre automáticamente. El valor predeterminado es 3000 ms (3 segundos).

  Eventos:
  - cerrar: Se emite cuando se cierra la alerta, ya sea manualmente (al hacer clic en el botón de cerrar) o automáticamente después del tiempo especificado.

-->
<template>
    <!-- https://vuejs.org/guide/built-ins/transition transiciones de componentes -->
    <transition name="fade">
      <v-alert
        v-if="mostrar"
        :type="tipo"
        dismissible
        class="alert-container ma-2"
        @input="cerrarAlerta"
      >
        {{ mensaje }}
      </v-alert>
    </transition>
  </template>
  
  <script>
  export default {
    name: "FadeAlert",
    props: {
      mostrar: {
        type: Boolean,
        required: true,
      },
      mensaje: {
        type: String,
        required: true,
      },
      tipo: {
        type: String,
        default: "success",
      },
      temporal: {
        type: Boolean,
        default: true,
      },
      tiempo: {
        type: Number,
        default: 3000,
      },
    },
    watch: {
      mostrar(val) {
        if (val && this.temporal) {
          this.cerrarAutomaticamente();
        }
      },
    },
    methods: {
      cerrarAlerta() {
        this.$emit("cerrar");
      },
      cerrarAutomaticamente() {
        setTimeout(() => {
          this.cerrarAlerta();
        }, this.tiempo);
      },
    },
  };
  </script>
  
  <style scoped>
  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.5s ease;
  }
  
  .fade-enter-from,
  .fade-leave-to {
    opacity: 0;
  }
  
  .alert-container {
    position: fixed;
    top: 10px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 9999;
    width: 90%;
    max-width: 500px;
  }
  </style>
  