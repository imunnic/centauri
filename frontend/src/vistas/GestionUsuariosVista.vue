<template>
  <div>
    <ListaCrudComponent 
      class="cursor-normal"
      :items="usuarios"
      @editar=""
      @detalle=""
      @crear=""
      :cargando="cargando"
      @eliminar=""
      :permisoCreacion="false"
      :permisoEdicion="false">
      <template v-slot:titulo="{ item }">
        <v-icon icon="mdi-account"></v-icon>
        {{ item.nombre }}  
        <div class="contenedor-usuario">
          <v-select 
              v-model="item.rol" 
              :items="perfiles" 
              class="input-medio"
              @update:modelValue="cambiarPerfil(item)"/>
        </div>
        </template>
     </ListaCrudComponent>
  </div>
</template>
<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
import { useAlertasStore } from "@/store/alertasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapActions } from "pinia";
export default {
  components: { ListaCrudComponent },
  data() {
    return {
      usuarios: [],
      cargando:false,
      perfiles:['ECEF', 'DIPLOMADO','USUARIO']
    };
  },
  methods: {
    ...mapActions(useUsuariosStore, ["getUsuarios","cambiarRol"]),
    ...mapActions(useAlertasStore, ["mostrarExito", "mostrarError"]),
    async cambiarPerfil(usuario){
      try {

        await this.cambiarRol(usuario);
        await this.cargarUsuarios();
        this.mostrarExito('Perfil cambiado con éxito');
      } catch (error) {
        this.mostrarError('No se ha podido cambiar el perfil');
      }
    },
    async cargarUsuarios(){
      this.cargando = true;
      this.usuarios = await this.getUsuarios();
      this.cargando = false;
    }
  },
  async created() {
    await this.cargarUsuarios();
  },
};
</script>
<style scoped>
.contenedor-usuario{
  display: flex;
  flex-flow: row;
  gap: 10px;
  align-items: center;
  overflow-x: scroll;
}
</style>
