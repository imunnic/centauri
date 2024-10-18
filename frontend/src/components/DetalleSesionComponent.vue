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
      <div class="titulo-sesion">
        {{ sesion.nombre || "Sin nombre" }}
        <div class="contenedor-flex iconos">
          <v-icon
            v-if="permisoEdicion"
            @click="editarSesion"
            class="icono-editar"
          >
            mdi-pencil
          </v-icon>
          <v-icon
            v-if="permisoEdicion"
            @click="borrarSesion"
            class="icono-borrar"
          >
            mdi-trash-can
          </v-icon>
          <v-icon @click="detalle" class="icono-editar"> mdi-eye </v-icon>
        </div>
      </div>
    </v-card-title>

    <v-card-text class="contenido">
      <b>Grupo: </b>{{ sesion.grupo.nombre }}
      <v-list dense>
        <v-list-item v-for="(ficha, index) in sesion.fichas || []" :key="index">
          <v-card class="mb-2 claro" outlined>
            <v-card-title>{{ ficha.nombre }}</v-card-title>
          </v-card>
        </v-list-item>
      </v-list>
    </v-card-text>

    <v-divider v-if="puedeRealizar && !realizada" />
    <v-form
      v-if="puedeRealizar && !realizada"
      ref="form"
      @submit.prevent=""
      class="formulario"
    >
      <p>Notas de la sesión</p>

      <div class="input-con-icono">
        <v-text-field
          class="placeholder input-corto"
          placeholder="5"
          v-model="rpe"
          label="RPE"
          type="number"
          :rules="[
            (v) => !!v || 'El RPE es necesario',
            (v) => (v >= 1 && v <= 10) || 'El RPE debe estar entre 1 y 10',
          ]"
          :min="1"
          :max="10"
          required
        ></v-text-field>
        <InformacionComponent :texto="ayudaRpe"></InformacionComponent>
      </div>

      <div class="input-con-icono">
        <v-text-field
          class="placeholder input-corto"
          placeholder="35"
          v-model="tiempo"
          type="number"
          label="Tiempo"
          :rules="[(v) => !!v || 'El tiempo de la sesión es necesario',
          (v) => (v >= 1) || 'El tiempo debe ser superior a 1',
          ]"
          :min="1"
          required
        ></v-text-field>
        <InformacionComponent :texto="ayudaTiempo"></InformacionComponent>
      </div>

      <v-textarea
        class="placeholder"
        placeholder="En esta sesión realicé 3 repeticiones del circuito"
        v-model="comentarios"
        label="Comentarios"
      ></v-textarea>
    </v-form>

    <v-card-actions class="botones">
      <v-btn
        v-if="puedeRealizar && !realizada"
        aria-label="realizar-sesion"
        class="claro"
        @click="hecha"
        >Hecha</v-btn
      >
    </v-card-actions>
  </v-card>
</template>

<script>
import InformacionComponent from "@/components/comun/InformacionComponent.vue";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapState, mapActions } from "pinia";
import tooltips from "@/tooltips.json";
export default {
  components:{InformacionComponent},
  props: {
    sesion: {
      type: Object,
      required: true,
    },
    gruposConPermiso: {
      type: Array,
      required: true,
      default: [],
    },
  },
  computed: {
    ...mapState(useUsuariosStore, ["href"]),
    permisoEdicion() {
      return this.gruposConPermiso.some(
        (grupo) => grupo.nombre === this.sesion.grupo.nombre
      );
    },
    puedeRealizar() {
      const partesFecha = this.sesion.fecha.split("/");
      const dia = parseInt(partesFecha[0], 10);
      const mes = parseInt(partesFecha[1], 10) - 1;
      const año = parseInt(partesFecha[2], 10);
      const fechaSesionDate = new Date(año, mes, dia);
      const hoy = new Date();
      hoy.setHours(0, 0, 0, 0);
      return fechaSesionDate <= hoy;
    },
  },
  data() {
    return {
      rpe: 1,
      tiempo: 10,
      comentarios: "",
      ayudaRpe: tooltips.ayudaRPE,
      ayudaTiempo: tooltips.ayudaTiempo,
      realizada: false,
    };
  },
  methods: {
    ...mapActions(useSesionesRealizadasStore, ["comprobarSesionRealizada"]),
    cerrarTarjeta() {
      this.$emit("cerrarTarjeta");
    },
    editarSesion() {
      this.$emit("editarSesion", this.sesion);
    },
    borrarSesion() {
      this.$emit("borrarSesion", this.sesion);
    },
    detalle() {
      this.$emit("detalle", this.sesion.href);
    },
    async hecha() {
      let valido = await this.$refs.form.validate();
      if (valido.valid) {
        let sesionRealizada = {
          sesion: this.sesion.href,
          rpe: this.rpe,
          tiempo: this.tiempo,
          comentarios: this.comentarios,
        };
        this.$emit("hecha", sesionRealizada);
      }
    },
  },
  async created() {
    this.realizada = await this.comprobarSesionRealizada(
      this.href,
      this.sesion.href
    );
  },
};
</script>

<style scoped>
.titulo-sesion {
  font-weight: bold;
  display: flex;
  flex-flow: row;
  width: 100%;
}
.espacio-completo {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
.contenedor-flex {
  justify-content: flex-start;
  width: 100%;
}

.icono-editar,
.icono-borrar {
  cursor: pointer;
  color: gray;
}

.icono-editar:hover {
  color: var(--claro);
}

.icono-borrar:hover {
  color: var(--rechazo);
}
.iconos {
  display: flex;
  justify-content: end;
  padding-left: 1rem;
}
.contenido {
  width: 100%;
}
.botones {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}
.formulario {
  margin: 15px;
}

.input-con-icono {
  display: flex;
  align-items: center;
}

.input-con-icono .v-text-field {
  flex-grow: 1;
}

.input-con-icono .v-icon {
  margin-left: 8px;
  cursor: pointer;
  color: gray;
}
</style>
