<!--
  Este componente muestra la información detallada de una sesión específica en el calendario. 
  Si el usuario tiene permisos de edición, puede editar o eliminar la sesión. Además, permite emitir 
  eventos cuando se selecciona una acción como ver el detalle de la sesión, editarla, eliminarla o cerrar la tarjeta.

  Props:
  - sesion: Un objeto que contiene la información de la sesión seleccionada, como nombre, grupo y fichas asociadas.
  - gruposConPermiso: Un array de objetos que contiene los grupos con permisos de edición, usado para determinar si el usuario puede editar o eliminar la sesión.

  Computed:
  - permisoEdicion: Retorna un booleano que indica si el grupo actual de la sesión está en la lista de grupos con permisos de edición.

  Eventos:
  - cerrarTarjeta: Se emite cuando el usuario cierra la tarjeta.
  - editarSesion: Se emite cuando el usuario selecciona la opción de editar la sesión, entregando el objeto de la sesión como payload.
  - borrarSesion: Se emite cuando el usuario selecciona la opción de eliminar la sesión, entregando el objeto de la sesión como payload.
  - detalle: Se emite cuando el usuario selecciona la opción de ver la sesión completa, entregando el objeto de la sesión como payload.
-->

<template>
  <v-card class="contenedor-flex">
    <v-card-title class="espacio-completo">
      <span class="titulo-sesion">
        {{ sesion.nombre || "Sin nombre" }}
      </span>
      <v-icon @click="cerrarTarjeta" class="icono-cerrar">mdi-close</v-icon>
    </v-card-title>

    <div class="contenedor-flex iconos">
      <v-icon 
        v-if="permisoEdicion" 
        @click="editarSesion" 
        class="icono-editar mr-2">
        mdi-pencil
      </v-icon>
      <v-icon 
        v-if="permisoEdicion" 
        @click="borrarSesion" 
        class="icono-borrar">
        mdi-trash-can
      </v-icon>
    </div>

    <v-card-text class="contenido">
      <p>{{ sesion.grupo.nombre }}</p>
      <v-list dense>
        <v-list-item v-for="(ficha, index) in sesion.fichas || []" :key="index">
          <v-card class="mb-2 claro" outlined>
            <v-card-title>{{ ficha.nombre }}</v-card-title>
          </v-card>
        </v-list-item>
      </v-list>
    </v-card-text>

    <v-card-actions class="botones">
      <v-btn aria-label="ver-sesion" class="claro" @click="detalle">Ver sesión completa</v-btn>
      <v-btn aria-label="cerrar-tarjeta" class="claro" @click="cerrarTarjeta">Cerrar</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  props: {
    sesion: {
      type: Object,
      required: true,
    },
    gruposConPermiso: {
      type: Array,
      required: true,
      default:[]
    },
  },
  computed: {
    permisoEdicion() {
      return this.gruposConPermiso.some(grupo => grupo.nombre === this.sesion.grupo.nombre);
    }
  },
  methods: {
    cerrarTarjeta() {
      this.$emit("cerrarTarjeta");
    },
    editarSesion() {
      this.$emit("editarSesion", this.sesion);
    },
    borrarSesion() {
      this.$emit("borrarSesion", this.sesion);
    },
    detalle(){
      this.$emit("detalle", this.sesion);
    }
  },
};
</script>

<style scoped>
.titulo-sesion {
  font-weight: bold;
  text-decoration: underline;
}
.espacio-completo{
  display: flex;
  justify-content: space-between;
  width: 100%;
}
.contenedor-flex{
  justify-content: flex-start;
  width: 100%
}
.icono-cerrar,
.icono-editar,
.icono-borrar {
  cursor: pointer;
  color: gray;
}

.icono-editar:hover{
  color:var(--claro)
}

.icono-borrar:hover {
  color: var(--rechazo);
}
.iconos{
  padding-left: 1rem;
}
.contenido{
  width: 100%
}
.botones{
  width: 100%;
  justify-self: flex-end;
}
</style>
