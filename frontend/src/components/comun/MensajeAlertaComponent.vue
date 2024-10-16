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
  