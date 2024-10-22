<template>
  <div v-if="cargando" class="circulo-carga">
    <v-progress-circular
      :size="70"
      :width="7"
      indeterminate
      color="primary"
    ></v-progress-circular>
  </div>
  <div class="claro" v-if="!sesionFin && !cargando && sesion">
    <ContadorSesionComponent
      :sesion="sesion"
      :marcas="marcas"
      class="claro"
      @sesion-finalizada="sesionFin = true"
    />
  </div>
  <div class="claro" v-if="!sesionFin && !cargando && ficha">
    <ContadorFichaComponent
      :ficha="ficha"
      :marcas="marcas"
      class="claro"
      @ficha-finalizada="sesionFin = true"
    />
  </div>
  <div class="claro" v-if="sesionFin">
    <p class="texto">Sesion terminada</p>
  </div>
</template>

<script>
import ContadorSesionComponent from "@/components/ContadorSesionComponent.vue";
import ContadorFichaComponent from "@/components/ContadorFichaComponent.vue";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useFichasStore } from "@/store/fichasStore";
import { mapActions } from "pinia";

export default {
  components: { ContadorSesionComponent, ContadorFichaComponent },
  data() {
    return {
      sesion: null,
      ficha:null,
      marcas: {
        "push press": 70,
        vam: 240,
        burpees: 50,
        "pull-ups":30,
        "push-ups":70,
        lunges:100
      },
      sesionFin: false,
      cargando: false,
    };
  },
  methods: {
    ...mapActions(useSesionesStore, [
      "getFichasDeSesionConId",
      "getSesionPorId",
    ]),
    ...mapActions(useFichasStore, ["cargarFichaDetalle"]),
  },
  async created() {
    if (this.$route.query.sesion == "true") {
      this.cargando = true;
      this.sesion = await this.getSesionPorId(this.$route.params.id);
      let fichas = await this.getFichasDeSesionConId(this.$route.params.id);
      this.sesion.fichas = fichas;
      this.cargando = false;
    } else {
      this.cargando = true;
      let response = await this.cargarFichaDetalle(this.$route.params.id)
      this.ficha = response.data;
      console.log(this.ficha);
      this.cargando = false;
    }
  },
};
</script>
<style scoped>
.claro:hover {
  background-color: var(--claro) !important;
}
</style>
