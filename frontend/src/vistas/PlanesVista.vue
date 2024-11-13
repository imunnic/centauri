<template>
  <div>
    <ListaCrudComponent
      :items="planes"
      :cargando="cargando"
      :permisoCreacion="false"
      :permisoEdicion="false"
      @detalle="verPlan"
    >
    </ListaCrudComponent>
    </div>
</template>
<script>
import ListaCrudComponent from '@/components/comun/ListaCrudComponent.vue';
import { usePlanesStore } from '@/store/planesStore.js';
import { mapActions } from 'pinia';
export default {
  components:{
    ListaCrudComponent
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
