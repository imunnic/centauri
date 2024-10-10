<!--
  Este componente permite la visualización de sesiones en un calendario, con la opción de cambiar 
  entre la vista de mes y día. Además, integra un selector de fecha y un detalle de sesiones seleccionadas. 
  Se apoya en la librería Vuetify (https://vuetifyjs.com/).

  Componentes:
  - SelectorFechaComponent: Componente para seleccionar una fecha y cambiar el modo de 
    visualización (mes o día). Recibe la propiedad `fecha` y emite eventos `cambiar-fecha` y 
    `cambiar-modo`.
  - CuerpoCalendarioComponent: Componente que representa el calendario mensual. Recibe las propiedades 
    `fecha` y `sesiones`, y emite los eventos `fecha-seleccionada` para seleccionar una fecha y 
    `sesion-seleccionada` para abrir una sesión específica.
  - CuerpoCalendarioDiaComponent: Componente que representa el calendario en modo día. Recibe 
    las propiedades `fecha` y `sesiones`, y emite los eventos `fecha-seleccionada` y 
    `sesion-seleccionada` cuando se selecciona una sesión específica.
  - DetalleSesionComponent: Componente que muestra el detalle de una sesión seleccionada. Recibe 
    las propiedades `sesion` y `gruposConPermiso`, y emite los eventos `editarSesion`, `borrarSesion`, 
    `cerrar-tarjeta` y `detalle` para interactuar con la sesión seleccionada.

  Props:
  - sesiones: Array de sesiones que se muestran en el calendario. Cada sesión tiene una fecha, un 
    nombre y un listado de fichas asociadas.
  - modoInicial: Modo de visualización del calendario ("mes" o "día"), que define la 
    vista inicial del calendario. Se gestiona internamente mediante la variable `modo`.
  - gruposConPermiso: Array que define los grupos con permiso para ver o editar las sesiones en el detalle.

  Slots:
  - detalle-sesion: Slot que permite personalizar el contenido del detalle de la sesión mostrada en el diálogo.

  Eventos:
  - cambiar-fecha: Emitido por `SelectorFechaComponent` cuando se selecciona una nueva fecha.
  - cambiar-modo: Emitido por `SelectorFechaComponent` cuando se cambia entre las vistas de "mes" y "día".
  - fecha-seleccionada: Emitido por `CuerpoCalendarioComponent` y `CuerpoCalendarioDiaComponent` al seleccionar una fecha en el calendario.
  - sesion-seleccionada: Emitido por `CuerpoCalendarioComponent` y `CuerpoCalendarioDiaComponent` cuando se selecciona una sesión específica.
  - editarSesion: Emitido por `DetalleSesionComponent` al seleccionar la opción de editar una sesión.
  - borrarSesion: Emitido por `DetalleSesionComponent` al seleccionar la opción de borrar una sesión.
  - cerrar-tarjeta: Emitido por `DetalleSesionComponent` para cerrar la tarjeta de detalles de la sesión.
-->

<template>
  <v-container>
    <SelectorFechaComponent
      :fecha="fecha"
      @cambiar-fecha="cambiarFecha"
      @cambiar-modo="cambiarModo"
    />

    <CuerpoCalendarioComponent
      v-if="modo === 'mes'"
      :fecha="fecha"
      class="calendario"
      :sesiones="sesiones"
      @fecha-seleccionada="enviarFecha"
      @sesion-seleccionada="mostrarSesion"
    />
    <CuerpoCalendarioDiaComponent
      v-else
      :fecha="fecha"
      class="calendario"
      :sesiones="sesiones"
      @fecha-seleccionada="enviarFecha"
      @sesion-seleccionada="mostrarSesion"
    />

    <v-dialog v-model="mostrarTarjeta" max-width="400">
      <v-card class="tarjeta">
        <v-icon @click="cerrarTarjeta" class="icono-cerrar">mdi-close</v-icon>
        <slot name="detalle-sesion">
          
        </slot>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import SelectorFechaComponent from "./SelectorFechaComponent.vue";
import CuerpoCalendarioComponent from "./CuerpoCalendarioComponent.vue";
import CuerpoCalendarioDiaComponent from "./CuerpoCalendarioDiaComponent.vue";

export default {
  components: {
    SelectorFechaComponent,
    CuerpoCalendarioComponent,
    CuerpoCalendarioDiaComponent,
  },
  props: {
    sesiones: {
      type: Array,
      required: true,
    },
    modoInicial: {
      type: String,
      default: "mes",
    },
    gruposConPermiso: {
      type: Array,
      required: true,
      default:[]
    },
  },
  data() {
    return {
      fecha: new Date(),
      mostrarTarjeta: false,
      sesionSeleccionada: {},
      modo: this.modoInicial,
    };
  },
  methods: {
    enviarFecha(fecha) {
      this.$emit("fecha-seleccionada", fecha);
    },
    cambiarFecha(direccion) {
      if (this.modo === "dia") {
        this.fecha = new Date(
          this.fecha.setDate(this.fecha.getDate() + direccion)
        );
      } else if (this.modo === "mes") {
        this.fecha = new Date(
          this.fecha.setMonth(this.fecha.getMonth() + direccion)
        );
      }
    },
    cambiarModo(nuevoModo) {
      this.modo = nuevoModo;
      this.$emit("modo-cambiado", nuevoModo);
    },
    mostrarSesion(sesion) {
      this.$emit("sesion-seleccionada", sesion);
      this.sesionSeleccionada = sesion;
      this.mostrarTarjeta = true;
    },
    borrarSesion(sesion){
      this.$emit("borrarSesion", sesion.href);
      this.mostrarTarjeta = false;
    },
    editarSesion(sesion){
      this.$emit("editarSesion", sesion);
      this.mostrarTarjeta = false;
    },
    cerrarTarjeta() {
      this.mostrarTarjeta = false;
    }
  },
};
</script>

<style scoped>
.calendario {
  width: 100%;
}
.icono-cerrar{
  padding: 10px;
  cursor: pointer;
  color: gray;
  align-self: flex-end;
}
.tarjeta{
  display: flex;
}
</style>
