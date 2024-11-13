<template>
  <div class="contenedor-flex agenda">
    <div class="izquierda formulario">
      <b>Plan de entrenamiento</b>
      <v-text-field
        label="Nombre"
        readonly=true
        placeholder="Plan TGCF - 2 meses"
        v-model="plan.nombre"
      ></v-text-field>
      <v-text-field
        label="Objetivo"
        readonly=true
        placeholder="TGCF"
        v-model="plan.objetivo"
      ></v-text-field>
      <v-textarea
        label="Descripción"
        placeholder="Preparar el TGCF en un margen de 2 meses"
        readonly=true
        v-model="plan.descripcion"
      ></v-textarea>
      <v-btn v-if="false" class="claro" @click="nuevoPlan">Crear Plan</v-btn>
    </div>
    <div class="derecha">
      <CalendarioSinFecha
        ref="calendar"
        :sesiones="plan.sesiones"
        @fecha-seleccionada=""
        @sesion-seleccionada="onSesionSeleccionada"
        :key="calendario"
      >
        <template v-slot:detalle-sesion>
          <DetalleSesionComponent
            :plan="true"
            :sesion="sesionSeleccionada"
            @editarSesion=""
            @borrarSesion=""
            @detalle=""
            @detalle-ficha="verFicha"
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
import { mapActions, mapState } from "pinia";
export default {
  components: {
    CalendarioSinFecha,
    SesionFormComponent,
    DetalleSesionComponent,
  },
  data() {
    return {
      id: "",
      plan: {},
      sesionSeleccionada:{}
    };
  },
  methods: {
    ...mapActions(usePlanesStore, ["getPlan"]),
    onSesionSeleccionada(sesion){
      this.sesionSeleccionada = sesion;
    },
    verFicha(ficha){
      let ruta = this.$router.resolve({name:'fichas'});
      ruta = ruta.href + "/" + ficha.id;
      window.open(ruta,'_blank');
    }
  },
  async created() {
    this.id = this.$route.params.id;
    this.plan = await this.getPlan(this.id);
    console.log(this.plan);
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
