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
      <v-btn class="claro" @click="nuevoPlan">{{textoGuardado}}</v-btn>
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
            :crearPlan="true"
            @cerrarTarjeta="cerrarDetalleCalendario"
            @borrarSesion="borrarSesion"
            @detalle-ficha="verFicha"
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
import { useFichasStore } from "@/store/fichasStore.js";
import { useAlertasStore } from "@/store/alertasStore.js";
import { mapState, mapActions } from "pinia";

export default {
  components: {
    CalendarioSinFecha,
    SesionFormComponent,
    DetalleSesionComponent,
  },
  computed: {
    ...mapState(useUsuariosStore, ["href","username"]),
    textoGuardado() {
      let texto = "";
      if (this.id) {
        texto = "Editar plan";
      } else {
        texto = "Crear plan";
      }
      return texto;
    },
    id() {
      return this.$route.query.id;
    },
  },
  data() {
    return {
      sesiones: [],
      formularioSesion: false,
      dia: 1,
      calendario: 1,
      sesionSeleccionada: {},
      nombre: "",
      objetivo: "",
      descripcion: "",
    };
  },
  methods: {
    ...mapActions(usePlanesStore, ["crearPlan", "getPlan", "modificarPlan"]),
    ...mapActions(useFichasStore, ["getFichaPorHref"]),
    ...mapActions(useAlertasStore, ["mostrarExito", "mostrarError"]),
    fechaSeleccionada(fecha) {
      this.formularioSesion = true;
      this.dia = fecha;
    },
    crearSesion(nuevaSesion) {
      nuevaSesion.dia = this.dia;
      this.sesiones.push(nuevaSesion);
      this.calendario++;
    },
    borrarSesion(sesionBorrada) {
      this.sesiones = this.sesiones.filter(
        (sesion) => sesion.nombre != sesionBorrada.nombre
      );
      this.cerrarDetalleCalendario();
    },
    async onSesionSeleccionada(sesion) {
      this.sesionSeleccionada = sesion;
      this.sesionSeleccionada.fichas = await Promise.all(
        this.sesionSeleccionada.fichas.map(
          async (href) => await this.getFichaPorHref(href)
        )
      );
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
        autor: this.username,
      };
      try {
        if (this.id) {
          plan.id = this.id;
          await this.modificarPlan(plan);
          this.$router.push("/planes");
        } else {
          await this.crearPlan(plan);
          this.$router.push("/planes");
        }
        this.mostrarExito("Plan guardado");
      } catch (error) {
        this.mostrarError("El plan no se ha podido guardar");
      }
    },
    verFicha(ficha) {
      let ruta = this.$router.resolve({ name: "fichas" });
      ruta = ruta.href + "/" + ficha.id;
      window.open(ruta, "_blank");
    },
  },
  async created() {
    const queryId = this.id;
    if (queryId) {
      let plan = await this.getPlan(this.id);
      this.sesiones = plan.sesiones;
      this.nombre = plan.nombre;
      this.objetivo = plan.objetivo;
      this.descripcion = plan.descripcion;
    }
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
