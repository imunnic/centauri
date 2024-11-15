<template>
  <div>
    <v-container>
      <v-text-field 
      prepend-inner-icon="mdi-magnify" 
      class="buscador"
      v-model="busqueda"
      @update:model-value="filtrarUsuarios"></v-text-field>
    </v-container>
    <ListaCrudComponent
      :busqueda="false"
      class="cursor-normal"
      :items="usuarios"
      :cargando="cargando"
      :permisoCreacion="false"
      :permisoEdicion="false"
    >
      <template v-slot:titulo="{ item }">
        <v-icon icon="mdi-account"></v-icon>
        {{ item.nombre }}
        <div class="contenedor-usuario">
          <v-select
            v-model="item.rol"
            :items="perfiles"
            class="input-medio"
            @update:modelValue="cambiarPerfil(item)"
          />
        </div>
      </template>
    </ListaCrudComponent>
    <div class="paginacion" v-if="mostrarPaginacion">
      <v-pagination
        class="numeros"
        @next="cambiarPagina"
        @prev="cambiarPagina"
        @update:model-value="cambiarPagina"
        :length="paginacion.paginasTotales"
      ></v-pagination
      ><v-select class="input-corto" :items="tamanosPagina" v-model="paginacion.tamano" @update:model-value="cambiarTamano"></v-select>
    </div>
  </div>
</template>
<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import { useAlertasStore } from "@/store/alertasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapActions } from "pinia";
export default {
  components: { ListaCrudComponent },
  computed:{
    mostrarPaginacion() {
      return this.busqueda.trim() === "";
    }
  },
  data() {
    return {
      usuarios: [],
      cargando: false,
      perfiles: ["ECEF", "DIPLOMADO", "USUARIO"],
      paginacion: {
        paginasTotales: 1,
        pagina: 0,
        tamano: 10,
      },
      busqueda:"",
      tamanosPagina: [10, 25, 50],
    };
  },
  methods: {
    ...mapActions(useUsuariosStore, ["getUsuarios", "cambiarRol", "getUsuariosBusqueda"]),
    ...mapActions(useAlertasStore, ["mostrarExito", "mostrarError"]),
    async cambiarPerfil(usuario) {
      try {
        await this.cambiarRol(usuario);
        await this.cargarUsuarios();
        this.mostrarExito("Perfil cambiado con éxito");
      } catch (error) {
        console.log(error)
        this.mostrarError("No se ha podido cambiar el perfil");
      }
    },
    async cargarUsuarios() {
      this.cargando = true;
      let respuesta = await this.getUsuarios(this.paginacion);
      this.usuarios = respuesta.usuarios;
      this.paginacion.paginasTotales = respuesta.paginasTotales;
      this.cargando = false;
    },
    async cambiarPagina(valor) {
      this.paginacion.pagina = valor - 1;
      await this.cargarUsuarios(this.paginacion);
    },
    async actualizarPagina(pagina) {
      this.paginacion.pagina = pagina - 1;
      await this.cargarUsuarios(this.paginacion);
    },
    async cambiarTamano(){
      this.paginacion.pagina = 0;
      await this.cargarUsuarios(this.paginacion);
    },
    async buscarUsuarios(){
      return await this.getUsuariosBusqueda(this.busqueda)
    },
    async filtrarUsuarios(){
      if (!this.mostrarPaginacion){
        this.usuarios = await this.buscarUsuarios();
      } else {
        await this.cargarUsuarios(this.paginacion);
      }
    }
  },
  async created() {
    await this.cargarUsuarios(this.paginacion);
  },
};
</script>
<style scoped>
.paginacion{
  display: flex;
  flex-flow: row;
  align-content: center;
  justify-content: center;
  align-items: start;
}
.numeros{
  width: 30%;
}
.contenedor-usuario {
  display: flex;
  flex-flow: row;
  gap: 10px;
  align-items: center;
  overflow-x: scroll;
}
</style>
