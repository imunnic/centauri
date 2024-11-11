<template>
  <v-container>
    <div class="contenedor-flex justify-start flex-fila">
      <v-switch
        aria-label="fichas-propias"
        v-if="perfil == 'ECEF' || perfil == 'DIPLOMADO'"
        v-model="fichasPropias"
        label="Mostrar fichas propias"
        color="var(--claro)"
        active-color="var(--claro)"
        class="interruptor"
      ></v-switch>
      <v-switch
        aria-label="fichas-pendientes"
        v-if="perfil == 'ECEF' && !fichasPropias"
        v-model="soloPendientes"
        label="Mostrar pendientes"
        color="var(--claro)"
        active-color="var(--claro)"
        class="interruptor"
      ></v-switch>
    </div>

    <ListaCrudComponent
      :items="fichasRegistradas"
      :key="fichasKey"
      @detalle="verFicha"
      :permisoCreacion="permisoCreacion"
      :permisoEdicion="fichasPropias"
      :cargando="cargando"
      @editar="editarFicha"
      @eliminar="eliminarFicha"
    >
      <template v-slot:info-extra="{ item }">
        <div class="info-relevante">
          <p>
            <b
              >RPE estimado:
              <span :style="{ color: getColor(item.rpeEstimado) }">
                {{ item.rpeEstimado }}/10
              </span>
            </b>
          </p>
          <p><b>Tiempo estimado:</b> {{ item.tiempoEstimado }} minutos</p>
          <p v-if="fichasPropias">
            <b
              >Estado:
              <span :style="{ color: getColor(item.estado) }">
                {{ item.estado }}
              </span>
            </b>
          </p>
        </div>
      </template>
    </ListaCrudComponent>

    <FabBotonComponent
      v-if="perfil == 'DIPLOMADO' || perfil == 'ECEF'"
      :icon="'mdi-plus'"
      @click="crearFicha"
    ></FabBotonComponent>
  </v-container>
</template>

<script>
import BuscadorComponent from "@/components/comun/BuscadorComponent.vue";
import ListaCrudComponent from "../components/comun/ListaCrudComponent.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";
import { useFichasStore } from "@/store/fichasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapActions, mapState } from "pinia";

export default {
  components: { BuscadorComponent, ListaCrudComponent, FabBotonComponent },
  computed: {
    ...mapState(useFichasStore, ["fichasRegistradas"]),
    ...mapState(useUsuariosStore, ["token", "perfil"]),
    permisoCreacion(){
      return this.perfil == "ECEF" || this.perfil == "DIPLOMADO";
    }
  },
  data() {
    return {
      fichasKey: 0,
      detalle: false,
      modoEdicion: false,
      misActions: [],
      cargando: false,
      soloPendientes: false,
      fichasPropias: false,
    };
  },
  async created() {
    this.arrancarServicioFicha(this.token);
  },
  async mounted() {
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
    },
  },
  methods: {
    ...mapActions(useFichasStore, [
      "arrancarServicioFicha",
      "cargarFichas",
      "cargarPendientes",
      "cargarPropias",
      "borrarFicha",
    ]),
    verFicha(ficha) {
      if (this.fichasPropias) {
        const href = ficha._links.self.href;
        const id = href.split("/").pop();
        this.$router.push("/fichas/" + id);
      } else {
        this.$router.push("/fichas/" + ficha.id);
      }
    },
    getColor(rpe) {
      if (rpe < 6 || rpe == "APROBADO") return "var(--suave)";
      if ((rpe >= 6 && rpe < 9) || rpe == "PENDIENTE") return "var(--elevado)";
      return "var(--rechazo)";
    },
    crearFicha() {
      this.$router.push("/fichas/crear");
    },
    editarFicha(fichaEditar) {
      const href = fichaEditar._links.self.href;
      const id = href.split("/").pop();
      this.$router.push({
        name: "editarFicha",
        params: { id },
        query: { edicion: true },
      });
    },
    async eliminarFicha(ficha) {
      await this.borrarFicha(ficha.id);
      await this.cargarPropias();
    },
  },
};
</script>

<style scoped>
.contenedor-flex{
  overflow: visible;
}
.flex-columna {
  align-items: start;
}
.info-relevante {
  font-size: 17px;
  padding-left: 15px;
}
.interruptor {
  margin: 5px;
  padding: 5px;
  max-height: 40px;
}
@media(max-width:500px){
  .flex-fila{
    align-items: start;
    flex-flow: column !important;
  }
}
</style>
