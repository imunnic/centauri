<template>
  <v-container class="contenedor-flex columna">
    <DatosLoginUsuarioComponent />

    <DatosMarcasUsuarioComponent 
      :marcasArray="marcasArray" 
      @actualizarMarcasArray="actualizarMarcasArray" 
    />

  </v-container>
</template>

<script>
import DatosLoginUsuarioComponent from "@/components/DatosLoginUsuarioComponent.vue"
import DatosMarcasUsuarioComponent from "@/components/DatosMarcasUsuarioComponent.vue";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapState } from "pinia";

export default {
  components: {
    DatosLoginUsuarioComponent,
    DatosMarcasUsuarioComponent,
  },
  computed: {
    ...mapState(useUsuariosStore, ["marcas"]),
  },
  methods: {
    actualizarMarcasArray() {
      this.marcasArray = Object.entries(this.marcas).map(([key, value]) => ({
        nombre: key,
        cantidad: value,
      }));
    },
  },
  data() {
    return {
      marcasArray: [],
    };
  },
  created() {
    this.actualizarMarcasArray();
  },
};
</script>
<style scoped>
.columna{
  flex-flow: column;
}
@media (min-width: 1000px) {
  .columna {
    flex-flow: row;
  }
}
</style>
