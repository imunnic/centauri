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
    <SesionRealizadaComponent 
    v-if="puedeRealizar && !realizada" 
    @sesion-realizada="hecha"
    @salir="cerrarTarjeta">
  </SesionRealizadaComponent>
  </v-card>
</template>

<script>
import InformacionComponent from "@/components/comun/InformacionComponent.vue";
import ValoracionComponent from "@/components/comun/ValoracionComponent.vue";
import SesionRealizadaComponent from "@/components/SesionRealizadaComponent.vue";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { mapState, mapActions } from "pinia";
import tooltips from "@/tooltips.json";
export default {
  components:{InformacionComponent, ValoracionComponent, SesionRealizadaComponent},
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
      realizada: true,
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
    async hecha(sesionRealizada) {
      sesionRealizada.sesion = this.sesion.href;
      this.$emit("hecha", sesionRealizada);
    },
    ajustarRpe(valor){
      this.rpe = valor*2;
    }
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
