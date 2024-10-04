<!--
  Este componente muestra un día específico en el calendario, con la lista de sesiones programadas 
  para ese día. Si es el día actual, se resalta visualmente. Además, permite emitir un evento cuando 
  se selecciona una sesión.

  Props:
  - fecha: La fecha correspondiente al día que se está mostrando.
  - sesiones: Array de sesiones programadas, de las cuales se filtran aquellas que pertenecen al día actual.

  Computed:
  - fechaActual: Devuelve el día, mes y año actuales, usados para resaltar el día presente.
  - dia: Devuelve el día y la fecha completa del día mostrado en el calendario.
  - sesionesDelDia: Filtra las sesiones para obtener solo las que coinciden con el día mostrado.

  Métodos:
  - emitirEvento: Emite el evento `sesion-seleccionada` al hacer clic en una sesión, pasando la sesión seleccionada como payload.
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
