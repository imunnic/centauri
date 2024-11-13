<template>
  <div class="calendario">
    <SelectorFechaComponent
      :sin-fecha="true"
      :dia="dia"
      @cambiar-fecha="cambiarFecha"
      @cambiar-modo="cambiarModo"
      :fecha="fecha"
    ></SelectorFechaComponent>
    <CuerpoCalendarioSinFechaComponent
      ref="calendario"
      :sesiones="sesiones"
      :dia="dia"
      :modo="modo"
      @fecha-seleccionada="fechaSeleccionada"
      @sesion-seleccionada="sesionSeleccionada"
    ></CuerpoCalendarioSinFechaComponent>
    <v-dialog v-model="mostrarTarjeta" max-width="400">
      <v-card class="tarjeta">
        <v-icon @click="cerrarTarjeta" class="icono-cerrar">mdi-close</v-icon>
        <slot name="detalle-sesion"> </slot>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
import SelectorFechaComponent from "@/components/comun/SelectorFechaComponent.vue";
import CuerpoCalendarioSinFechaComponent from "@/components/comun/CuerpoCalendarioSinFechaComponent.vue";

export default {
  components: { SelectorFechaComponent, CuerpoCalendarioSinFechaComponent },
  props: {
    sesiones: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      dia: 1,
      modo: "mes",
      fecha: new Date(),
      mostrarTarjeta: false,
    };
  },
  methods: {
    cambiarFecha(direccion) {
      if (this.modo == "dia") {
        this.dia = this.dia + direccion;
      } else {
        this.dia = this.dia + direccion * 28;
        this.$refs.calendario.generarCalendario();
      }
    },
    cambiarModo(modo) {
      this.modo = modo;
      if (modo == "mes") {
        this.dia = Math.floor(this.dia / 28) * 28 + 1;
        this.$refs.calendario.generarCalendario();
      }
    },
    fechaSeleccionada(fecha) {
      this.$emit("fecha-seleccionada", fecha);
    },
    sesionSeleccionada(sesion) {
      this.$emit("sesion-seleccionada", sesion);
      this.mostrarTarjeta = true;
    },
    cerrarTarjeta() {
      this.mostrarTarjeta = false;
    },
  },
};
</script>
<style scoped>
.calendario {
  width: 100%;
}
</style>
