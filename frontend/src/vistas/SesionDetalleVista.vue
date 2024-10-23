<template>
  <div>
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
  </div>
</template>
<script>
import FichaComponent from "@/components/FichaComponent.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
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
    ejecutar(){
      this.$router.push("/contador/" + this.$route.params.id + "?sesion=true")
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
<style scoped></style>
