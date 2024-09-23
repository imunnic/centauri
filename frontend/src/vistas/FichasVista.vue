<template>
  <v-container>
    <div class="contenedor-flex justify-start flex-columna">
      <v-switch v-if="perfil == 'ECEF' || perfil == 'DIPLOMADO'" v-model="fichasPropias" 
        label="Mostrar fichas propias" color="blue" active-color="blue" class="interruptor" 
        ></v-switch>
      <v-switch v-if="perfil == 'ECEF' && !fichasPropias" v-model="soloPendientes" label="Mostrar pendientes" color="red"
        active-color="red" class="interruptor"></v-switch>
    </div>

    <ListaCrudComponent :items="fichasRegistradas" :key="fichasKey" @detalle="verFicha" 
      :permiso-creacion="fichasPropias" :cargando="cargando">
      <template v-slot:info-extra="{ item }">
        <div class="info-relevante">
          <p>
            <b>RPE estimado:
              <span :style="{ color: getColor(item.rpeEstimado) }">
                {{ item.rpeEstimado }}/10
              </span>
            </b>
          </p>
          <p><b>Tiempo estimado:</b> {{ item.tiempoEstimado }} minutos</p>
          <p v-if="fichasPropias">
            <b>Estado: 
            <span :style="{ color: getColor(item.estado) }">
                {{ item.estado }}
              </span>
            </b>
          </p>
        </div>
      </template>
    </ListaCrudComponent>

    <FabBotonComponent v-if="perfil == 'DIPLOMADO' || perfil == 'ECEF'" :icon="'mdi-plus'" @click="crearFicha"></FabBotonComponent>
  </v-container>
</template>

<script>
import BuscadorComponent from '@/components/comun/BuscadorComponent.vue';
import ListaCrudComponent from '../components/comun/ListaCrudComponent.vue';
import FabBotonComponent from '@/components/comun/FabBotonComponent.vue';
import { useFichasStore } from '@/store/fichasStore.js';
import { useUsuariosStore } from '@/store/usuariosStore.js';
import { mapActions, mapState } from 'pinia';

export default {
  components: { BuscadorComponent, ListaCrudComponent, FabBotonComponent },
  computed: {
    ...mapState(useFichasStore, ['fichasRegistradas']),
    ...mapState(useUsuariosStore, ['token', 'perfil']),
  },
  data() {
    return {
      fichasKey: 0,
      detalle: false,
      modoEdicion: false,
      misActions: [],
      cargando: false,
      soloPendientes: false,
      fichasPropias: false
    };
  },
  async mounted() {
    this.arrancarServicioFicha(this.token);
    this.cargando = true;
    await this.cargarFichas();
    this.cargando = false;
  },
  watch: {
    async soloPendientes(newValue) {
      if (newValue) {
        await this.cargarPendientes();
      } else {
        await this.cargarFichas();
      }
    },
    async fichasPropias(newValue) {
      if (newValue) {
        this.soloPendientes = false;
        await this.cargarPropias();
      } else {
        await this.cargarFichas();
      }
    }
  },
  methods: {
    ...mapActions(useFichasStore, ['arrancarServicioFicha', 'cargarFichas', 'cargarPendientes','cargarPropias']),
    verFicha(ficha) {
      const href = ficha.id;
      const id = href.split('/').pop();
      this.$router.push('/fichas/' + id);
    },
    getColor(rpe) {
      if (rpe < 6 || rpe == 'APROBADO') return 'green';
      if ((rpe >= 6 && rpe < 9) || rpe == 'PENDIENTE') return 'orange';
      return 'red';
    },
    crearFicha(){
      this.$router.push('/fichas/crear');
    },

  }
};
</script>


<style scoped>
.flex-columna{
  align-items: start;
}
.info-relevante {
  font-size: 17px;
}
.interruptor{
  max-height: 40px;
}

</style>
