<template>
  <div class="contenedor-flex agenda">
    <div class="izquierda formulario">
      <b>Plan de entrenamiento</b>
      <v-text-field
        label="Nombre"
        placeholder="Plan TGCF - 2 meses"
        v-model="nombre"
      ></v-text-field>
      <v-text-field 
      label="Objetivo" 
      placeholder="TGCF"
      v-model="objetivo"
      ></v-text-field>
      <v-textarea
        label="Descripción"
        placeholder="Preparar el TGCF en un margen de 2 meses"
        v-model="descripcion"
      ></v-textarea>
      <v-btn class="claro" @click="nuevoPlan">Crear Plan</v-btn>
    </div>
    <div class="derecha">
      <CalendarioSinFecha
        ref="calendar"
        :sesiones="sesiones"
        @fecha-seleccionada="fechaSeleccionada"
        @sesion-seleccionada="onSesionSeleccionada"
        :key="calendario"
      >
        <template v-slot:detalle-sesion>
          <DetalleSesionComponent
            :plan="true"
            :sesion="sesionSeleccionada"
            @cerrarTarjeta="cerrarDetalleCalendario"
            @editarSesion=""
            @borrarSesion=""
            @detalle=""
            @hecha=""
          />
        </template>
      </CalendarioSinFecha>
      <SesionFormComponent
        v-if="formularioSesion"
        :plan="true"
        @cerrar="formularioSesion = false"
        @sesion-creada="crearSesion"
      ></SesionFormComponent>
    </div>
  </div>
</template>
<script>
import CalendarioSinFecha from "@/components/comun/CalendarioSinFecha.vue";
import SesionFormComponent from "@/components/SesionFormComponent.vue";
import DetalleSesionComponent from "@/components/DetalleSesionComponent.vue";
import { usePlanesStore } from "@/store/planesStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapState, mapActions } from "pinia";

export default {
  components: {
    CalendarioSinFecha,
    SesionFormComponent,
    DetalleSesionComponent,
  },
  computed: {
    ...mapState(useUsuariosStore, ["href"]),
  },
  data() {
    return {
      sesiones: [
      ],
      formularioSesion: false,
      dia: 1,
      calendario: 1,
      sesionSeleccionada: {},
      nombre:"",
      objetivo:"",
      descripcion:"",
    };
  },
  methods: {
    ...mapActions(usePlanesStore, ["crearPlan"]),
    fechaSeleccionada(fecha) {
      this.formularioSesion = true;
      this.dia = fecha;
    },
    crearSesion(nuevaSesion) {
      nuevaSesion.dia = this.dia;
      this.sesiones.push(nuevaSesion);
      this.calendario++;
      console.log(this.sesiones);
    },
    onSesionSeleccionada(sesion) {
      console.log(sesion);
      this.sesionSeleccionada = sesion;
    },
    cerrarDetalleCalendario() {
      this.$refs.calendar.mostrarTarjeta = false;
    },
    async nuevoPlan() {
      let plan = {
        nombre: this.nombre,
        objetivo: this.objetivo,
        descripcion: this.descripcion,
        sesiones: this.sesiones,
        autor: this.autor,
      };
      await this.crearPlan(plan);
      this.$router.push("/planificacion");
    },
  },
};
</script>
<style scoped>
.izquierda {
  width: 100%;
  display: flex;
  justify-content: start;
}

.derecha {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  min-width: 50%;
  padding-left: 20px;
}

.agenda {
  align-items: start;
}

.formulario {
  max-width: 20vw;
  display: flex;
  flex-flow: column;
  padding: 10px;
  margin-bottom: 10px;
}

@media (max-width: 1500px) {
  .agenda {
    flex-flow: column-reverse;
    align-items: center;
  }

  .grupos {
    max-width: 1200px;
    padding: 16px;
  }

  .izquierda {
    width: 100% !important;
  }

  .derecha {
    width: 100%;
    padding-left: 0;
  }
}
</style>
