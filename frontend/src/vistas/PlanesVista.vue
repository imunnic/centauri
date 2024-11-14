<template>
  <div>
    <ListaCrudComponent
      :items="planes"
      :cargando="cargando"
      :permisoCreacion="permisoCreacion"
      :permisoEdicion="false"
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
    ...mapState(useUsuariosStore,['perfil', 'isLogged']),
    permisoCreacion(){
      return ((this.perfil == 'ECEF' || this.perfil == 'DIPLOMADO') && this.isLogged)
    }
  },
  data() {
    return {
      planes:[],
      cargando:false
    }
  },
  methods:{
    ...mapActions(usePlanesStore, ['getPlanes']),
    verPlan(plan){
      this.$router.push("/planes/" + plan.id);
    },
    crearPlan(){
      this.$router.push("/planes/crear");

    }
  },
  async created(){
    this.cargando = true;
    this.planes = await this.getPlanes();
    this.cargando = false;
  },
};
</script>
<style scoped></style>
