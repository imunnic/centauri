<template>
  <div id="elementoSesion">
    <div v-if="calentamiento.length > 0" class="contenedor">
      <b>Calentamiento:</b>
      <FichaComponent
        :rondas="calentamiento"
        :solo-lectura="true"
      ></FichaComponent>
    </div>
    <div v-if="fundamental.length > 0" class="contenedor">
      <b>Parte fundamental:</b>
      <FichaComponent
        :rondas="fundamental"
        :solo-lectura="true"
      ></FichaComponent>
    </div>
    <div v-if="coordinacion.length > 0" class="contenedor">
      <b>Coordinación funcional:</b>
      <FichaComponent
        :rondas="coordinacion"
        :solo-lectura="true"
      ></FichaComponent>
    </div>
    <div v-if="vueltaCalma.length > 0" class="contenedor">
      <b>Vuelta a la Calma:</b>
      <FichaComponent
        :rondas="vueltaCalma"
        :solo-lectura="true"
      ></FichaComponent>
    </div>
    <FabBotonComponent icon="mdi-play-box-outline" @click="ejecutar"></FabBotonComponent>
    <FabBotonComponent class="imprimir" nombre="imprimir-sesion" icon="mdi-printer-outline" @click="imprimirSesion"></FabBotonComponent>
  </div>
</template>
<script>
import FichaComponent from "@/components/FichaComponent.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useImpresionStore } from "@/store/impresionStore";
import { mapActions } from "pinia";

export default {
  components: { FichaComponent, FabBotonComponent },
  data() {
    return {
      fichas: [],
      calentamiento: [],
      fundamental: [],
      coordinacion: [],
      vueltaCalma: [],
    };
  },
  methods: {
    ...mapActions(useSesionesStore, ["getFichasDeSesionConId"]),
    ...mapActions(useImpresionStore, ["imprimir"]),

    ejecutar(){
      this.$router.push("/contador/" + this.$route.params.id + "?sesion=true")
    },
    imprimirSesion(){
      this.imprimir("elementoSesion");
    }
  },
  async created() {},
  async mounted() {
    this.fichas = await this.getFichasDeSesionConId(this.$route.params.id);
    this.fichas.forEach((ficha) => {
      switch (ficha.parteSesion) {
        case "CALENTAMIENTO":
          this.calentamiento.push(...ficha.rutina);
          break;
        case "FUNDAMENTAL":
          this.fundamental.push(...ficha.rutina);
          break;
        case "COORDINACION":
          this.coordinacion.push(...ficha.rutina);
          break;
        case "CALMA":
          this.vueltaCalma.push(...ficha.rutina);
          break;
      }
    });
  },
};
</script>
<style scoped>
.imprimir{
  right: 100px;
}
</style>
