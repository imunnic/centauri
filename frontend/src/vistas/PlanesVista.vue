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
      @eliminar="eliminarPlan"
      @detalle="verPlan"
      @crear="crearPlan"
    >
    </ListaCrudComponent>
    </div>
</template>
<script>
import ListaCrudComponent from '@/components/comun/ListaCrudComponent.vue';
import { useUsuariosStore } from '@/store/usuariosStore';
import { usePlanesStore } from '@/store/planesStore.js';
import { mapState, mapActions } from 'pinia';
export default {
  components:{
    ListaCrudComponent
  },
  computed:{
    ...mapState(useUsuariosStore,['perfil', 'isLogged','username']),
    permisoCreacion(){
      return ((this.perfil == 'ECEF' || this.perfil == 'DIPLOMADO') && this.isLogged)
    }
  },
  data() {
    return {
      planes:[],
      cargando:false,
      planesPropios:false
    }
  },
  watch: {
    async planesPropios(newValue) {
      if (newValue) {
        this.planes = this.planes.filter(plan => plan.autor == this.username);
      } else {
        await this.cargarPlanes();
      }
    },
  },
  methods:{
    ...mapActions(usePlanesStore, ['getPlanes','borrarPlan']),
    verPlan(plan){
      this.$router.push("/planes/" + plan.id);
    },
    crearPlan(){
      this.$router.push("/planes/crear");

    },
    async cargarPlanes(){
      this.cargando = true;
      this.planes = await this.getPlanes();
      this.cargando = false;
    },
    async eliminarPlan(planBorrado){
      await this.borrarPlan(planBorrado.id)
      this.planes = this.planes.filter(plan => plan.id != planBorrado.id);
    }
  },
  async created(){
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
</style>
