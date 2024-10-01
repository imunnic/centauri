<!--
  Este componente permite la visualización de sesiones en un calendario, con la opción de cambiar 
  entre la vista de mes y día. Además, integra un selector de fecha y un detalle para ver las 
  sesiones seleccionadas. Se apoya en la librería de Vuetify (https://vuetifyjs.com/).

  Componentes:
  - SelectorFechaComponent: Componente que permite seleccionar una fecha y cambiar el modo de 
    visualización (mes o día). Recibe la propiedad `fecha` y emite eventos `cambiar-fecha` y 
    `cambiar-modo`.
  - CuerpoCalendarioComponent: Componente que representa el calendario mensual. Recibe la propiedad 
    `fecha` y `sesiones`, y emite los eventos `fecha-seleccionada` para seleccionar una fecha y 
    `sesion-seleccionada` para abrir una sesión específica.
  - CuerpoCalendarioDiaComponent: Componente que representa el calendario en modo día. Recibe 
    la propiedad `fecha` y `sesiones`, y emite el evento `sesion-seleccionada` cuando se selecciona 
    una sesión específica.
  - DetalleSesionComponent: Componente que muestra el detalle de una sesión seleccionada. Recibe 
    la propiedad `sesion` y emite el evento `cerrar-tarjeta` para cerrar el diálogo de detalles.

  Props:
  - sesiones: Array de sesiones que se muestran en el calendario. Cada sesión tiene una fecha, un 
    nombre y un listado de fichas asociadas.
  - modoInicial: Modo de visualización del calendario ("mes" o "día"), que se recibe como prop y define la 
    vista inicial del calendario. El valor inicial de la prop es gestionado internamente a través de 
    la variable `modo`.

  Data:
  - fecha: Fecha actual seleccionada para el calendario.
  - modo: Modo de visualización del calendario, gestionado internamente por el componente.
  - mostrarTarjeta: Booleano que indica si se debe mostrar la tarjeta de detalles de la sesión. 
  - sesionSeleccionada: Objeto que contiene la sesión seleccionada actualmente.

  Métodos:
  - enviarFecha: Emite la fecha seleccionada en el calendario.
  - cambiarFecha: Cambia la fecha en el calendario según el modo seleccionado (día o mes).
  - cambiarModo: Cambia entre los modos de vista "mes" y "día", y actualiza `modo`.
  - mostrarSesion: Muestra la tarjeta de detalles de una sesión seleccionada.
  - cerrarTarjeta: Cierra la tarjeta de detalles de la sesión.
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
      <DetalleSesionComponent
        :sesion="sesionSeleccionada"
        :gruposConPermiso="gruposConPermiso"
        @editarSesion="editarSesion"
        @borrarSesion="borrarSesion"
        @cerrarTarjeta="cerrarTarjeta"
      />
    </v-dialog>
  </v-container>
</template>

<script>
import SelectorFechaComponent from "./SelectorFechaComponent.vue";
import CuerpoCalendarioComponent from "./CuerpoCalendarioComponent.vue";
import DetalleSesionComponent from "./DetalleSesionComponent.vue";
import CuerpoCalendarioDiaComponent from "./CuerpoCalendarioDiaComponent.vue";

export default {
  components: {
    SelectorFechaComponent,
    CuerpoCalendarioComponent,
    DetalleSesionComponent,
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
    },
  },
};
</script>

<style scoped>
.calendario {
  width: 100%;
}
</style>
