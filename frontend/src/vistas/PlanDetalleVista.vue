<template>
  <div class="contenedor-flex agenda">
    <div class="izquierda formulario">
      <b>Plan de entrenamiento</b>
      <v-text-field
        label="Nombre"
        :readonly="true"
        placeholder="Plan TGCF - 2 meses"
        v-model="plan.nombre"
      ></v-text-field>
      <v-text-field
        label="Objetivo"
        :readonly="true"
        placeholder="TGCF"
        v-model="plan.objetivo"
      ></v-text-field>
      <v-textarea
        label="Descripción"
        placeholder="Preparar el TGCF en un margen de 2 meses"
        :readonly="true"
        v-model="plan.descripcion"
      ></v-textarea>
    </div>
    <div class="derecha">
      <CalendarioSinFecha
        ref="calendar"
        :sesiones="plan.sesiones"
        @fecha-seleccionada=""
        @sesion-seleccionada="onSesionSeleccionada"
      >
        <template v-slot:detalle-sesion>
          <DetalleSesionComponent
            :plan="true"
            :sesion="sesionSeleccionada"
            @detalle-ficha="verFicha"
          />
        </template>
      </CalendarioSinFecha>
    </div>
    <FabBotonComponent
      v-if="puedePlanificar"
      icon="mdi-calendar"
      @click="confirmarAgregarSesiones"
    ></FabBotonComponent>
  </div>
  <AgregarSesionesPlanForm
    v-if="agregarSesionesForm"
    :grupos="gruposEncargado"
    @confirmar="agregarSesionesPlan"
    @cerrar="agregarSesionesForm=false"
  ></AgregarSesionesPlanForm>
</template>
<script>
import AgregarSesionesPlanForm from "@/components/AgregarSesionesPlanForm.vue";
import CalendarioSinFecha from "@/components/comun/CalendarioSinFecha.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";
import SesionFormComponent from "@/components/SesionFormComponent.vue";
import DetalleSesionComponent from "@/components/DetalleSesionComponent.vue";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useGruposStore } from "@/store/gruposStore";
import { useFichasStore } from "@/store/fichasStore.js";
import { usePlanesStore } from "@/store/planesStore.js";
import { useAlertasStore } from "@/store/alertasStore.js";
import { mapActions, mapState } from "pinia";
export default {
  components: {
    CalendarioSinFecha,
    SesionFormComponent,
    DetalleSesionComponent,
    FabBotonComponent,
    AgregarSesionesPlanForm,
  },
  computed: {
    ...mapState(useUsuariosStore, ["isLogged"]),
    ...mapState(useGruposStore, ["gruposEncargado"]),
    puedePlanificar() {
      return this.isLogged && this.gruposEncargado.length > 0;
    },
  },
  data() {
    return {
      id: "",
      plan: {sesiones:[]},
      sesionSeleccionada: {},
      agregarSesionesForm: false,
    };
  },
  methods: {
    ...mapActions(usePlanesStore, ["getPlan","agregarSesiones"]),
    ...mapActions(useFichasStore, ["getFichaPorHref"]),
    ...mapActions(useAlertasStore, ["mostrarExito", "mostrarError"]),
    async onSesionSeleccionada(sesion) {
      this.sesionSeleccionada = sesion;
      // this.sesionSeleccionada.fichas = await Promise.all(
      //   this.sesionSeleccionada.fichas.map(
      //     async (href) => await this.getFichaPorHref(href)
      //   )
      // );
    },
    verFicha(ficha) {
      let ruta = this.$router.resolve({ name: "fichas" });
      ruta = ruta.href + "/" + ficha.id;
      window.open(ruta, "_blank");
    },
    confirmarAgregarSesiones() {
      console.log(this.gruposEncargado)
      this.agregarSesionesForm = true;
    },
    async agregarSesionesPlan(agregarPlanRequest){
      agregarPlanRequest.planId = this.$route.params.id;
      try {
        await this.agregarSesiones(agregarPlanRequest);
        this.mostrarExito("Sesiones cargadas al grupo " + agregarPlanRequest.grupo);
        this.$router.push("/planificacion")
      } catch (error) {
        console.log(error);
        this.mostrarError("No se han podido agregar las sesiones");
      }
    }

  },
  async created() {
    this.id = this.$route.params.id;
    this.plan = await this.getPlan(this.id);
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
