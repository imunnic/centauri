<template>
  <div>
    <v-container>
      <v-switch
        aria-label="planes-propios"
        v-if="perfil == 'ECEF' || perfil == 'DIPLOMADO'"
        v-model="planesPropios"
        label="Mostrar planes propios"
        color="var(--claro)"
        active-color="var(--claro)"
        class="interruptor"
      ></v-switch>
    </v-container>
    <ListaCrudComponent
      :items="planes"
      :cargando="cargando"
      :permisoCreacion="permisoCreacion"
      :permisoEdicion="planesPropios"
      @eliminar="confirmarEliminacion"
      @editar="editarPlan"
      @detalle="verPlan"
      @crear="crearPlan"
    >
    </ListaCrudComponent>
  </div>
  <v-dialog v-model="mostrarConfirmacionEliminar" max-width="600px">
      <v-card>
        <v-card-title class="flex-fila justify-space-between">
          <span>Confirmar eliminación</span>
          <v-btn icon flat @click="cancelarEliminacion">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          ¿Está seguro de que desea eliminar este plan? Esta acción no se puede deshacer.
        </v-card-text>
        <div class="contenedor-flex botones">
          <v-btn class="claro boton" @click="eliminarPlan">Confirmar</v-btn>
          <v-btn class="rechazo boton" @click="cancelarEliminacion">Cancelar</v-btn>
        </div>
      </v-card>
    </v-dialog>
</template>
<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import { useUsuariosStore } from "@/store/usuariosStore";
import { usePlanesStore } from "@/store/planesStore.js";
import { mapState, mapActions } from "pinia";
export default {
  components: {
    ListaCrudComponent,
  },
  computed: {
    ...mapState(useUsuariosStore, ["perfil", "isLogged", "username"]),
    permisoCreacion() {
      return (
        (this.perfil == "ECEF" || this.perfil == "DIPLOMADO") && this.isLogged
      );
    },
  },
  data() {
    return {
      planes: [],
      cargando: false,
      planesPropios: false,
      mostrarConfirmacionEliminar: false,
      planSeleccionado: null,
    };
  },
  watch: {
    async planesPropios(newValue) {
      if (newValue) {
        this.planes = this.planes.filter((plan) => plan.autor == this.username);
      } else {
        await this.cargarPlanes();
      }
    },
  },
  methods: {
    ...mapActions(usePlanesStore, ["getPlanes", "borrarPlan"]),
    verPlan(plan) {
      this.$router.push("/planes/" + plan.id);
    },
    crearPlan() {
      this.$router.push("/planes/crear");
    },
    async cargarPlanes() {
      this.cargando = true;
      this.planes = await this.getPlanes();
      this.cargando = false;
    },
    async eliminarPlan() {
      await this.borrarPlan(this.planSeleccionado.id);
      this.planes = this.planes.filter((plan) => plan.id != this.planSeleccionado.id);
      this.cancelarEliminacion();
    },
    editarPlan(plan) {
      this.$router.push("/planes/crear?id=" + plan.id);
    },
    confirmarEliminacion(plan) {
      this.planSeleccionado = plan;
      this.mostrarConfirmacionEliminar = true;
    },
    cancelarEliminacion() {
      this.mostrarConfirmacionEliminar = false;
      this.planSeleccionado = null;
    },
  },
  async created() {
    this.cargarPlanes();
  },
};
</script>
<style scoped>
.interruptor {
  margin: 5px;
  padding: 5px;
  max-height: 40px;
}
.botones {
  justify-content: end;
  margin: 10px;
}
.boton {
  margin: 10px;
}
</style>
