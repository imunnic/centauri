<template>
  <v-container fluid>
    <template v-if="modo === 'mes'">
      <v-row class="no-gutters">
        <v-col
          v-for="(dia, index) in diasDeLaSemana"
          :key="index"
          class="text-center"
          cols="auto"
        >
          <span class="texto-dia">{{ dia }}</span>
        </v-col>
      </v-row>

      <v-row
        class="no-gutters"
        v-for="(semana, index) in semanasDelMes"
        :key="index"
      >
        <v-col
          v-for="(dia, index) in semana"
          :key="index"
          class="d-flex flex-column align-center"
          cols="auto"
          style="border: 1px solid lightgray"
        >
          <v-card class="carta-dia" flat @click="emitirFecha(dia)">
            <v-card-title class="titulo-dia">
              <span>{{ dia.fecha }}</span>
            </v-card-title>

            <v-container class="lista-eventos" v-if="sesionesPorDia(dia).length">
              <v-card
                v-for="(sesion, sesionIndex) in sesionesPorDia(dia)"
                :key="sesionIndex"
                class="carta-evento claro"
                flat
                @click.stop="emitirEvento(sesion)"
              >
                <p class="evento-descripcion">{{ sesion.nombre }}</p>
              </v-card>
            </v-container>
          </v-card>
        </v-col>
      </v-row>
    </template>

    <template v-else-if="modo === 'dia'">
      <v-card class="carta-dia" elevation="2" @click="emitirFecha(dia)">
        <v-card-title
          class="titulo-dia"
          :class="{
            claro:
              dia.fecha &&
              dia.fecha === fechaActual.dia &&
              fecha.getMonth() === fechaActual.mes &&
              fecha.getFullYear() === fechaActual.año,
            redondeado:
              dia.fecha &&
              dia.fecha === fechaActual.dia &&
              fecha.getMonth() === fechaActual.mes &&
              fecha.getFullYear() === fechaActual.año,
          }"
        >
          <span v-if="dia.fecha">{{ dia.fecha }}</span>
        </v-card-title>

        <v-container class="lista-eventos">
          <template v-if="sesionesPorDia(dia).length > 0">
            <v-card
              v-for="(sesion, sesionIndex) in sesionesPorDia(dia)"
              :key="sesionIndex"
              class="carta-eventos claro"
              flat
              @click.stop="emitirEvento(sesion)"
              :style="{
                backgroundColor: sesion.grupo.color + ' !important',
              }"
            >
              {{ sesion.nombre }} - {{ sesion.grupo.nombre }}
            </v-card>
          </template>
          <template v-else>
            <v-card class="carta-eventos sin-sesiones" flat>
              No hay sesiones previstas para esta fecha. Pulse para crear una sesion.
            </v-card>
          </template>
        </v-container>
      </v-card>
    </template>
  </v-container>
</template>

<script>
export default {
  props: {
    sesiones: {
      type: Array,
      required: true,
      default: [],
    },
    dia: {
      type: Number,
    },
    modo: {
      type: String,
      required: true,
      default: 'mes',
    },
  },
  data() {
    return {
      diasDeLaSemana: [
        "Lunes",
        "Martes",
        "Miércoles",
        "Jueves",
        "Viernes",
        "Sábado",
        "Domingo",
      ],
      semanasDelMes: [],
      duracionMes: 28,
      fechaActual: {
        dia: new Date().getDate(),
        mes: new Date().getMonth(),
        año: new Date().getFullYear(),
      },
    };
  },
  watch: {
    sesiones: {
      handler() {
        this.generarCalendario();
      },
      deep: true,
    },
    dia: {
      handler() {
        this.generarCalendario();
      },
      deep: true,
    },
  },
  computed:{
    
  },
  methods: {
    generarCalendario() {
      const semanas = [];
      let diaActual = this.dia;

      for (let i = 0; i < 4; i++) {
        const semana = [];
        for (let j = 0; j < 7; j++) {
          semana.push({
            fecha: diaActual,
            esDeOtroMes: false,
          });
          diaActual++;
        }
        semanas.push(semana);
      }

      this.semanasDelMes = semanas;
    },

    sesionesPorDia(dia) {
      return this.sesiones.filter((sesion) => sesion.dia === dia.fecha);
    },

    emitirEvento(sesion) {
      this.$emit("sesion-seleccionada", sesion);
    },

    emitirFecha(dia) {
      this.$emit("fecha-seleccionada", dia.fecha);
    },
  },
  mounted() {
    this.generarCalendario();
  },
};
</script>

<style scoped>
.v-row {
  margin: 0;
}

.v-col {
  padding: 0;
}

.texto-dia {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.carta-dia {
  min-height: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  border: none;
  position: relative;
  overflow-y: hidden;
}

.titulo-dia {
  font-size: 16px;
  color: #000;
  margin-top: 8px;
}

.lista-eventos {
  margin-top: 10px;
  max-height: 70px;
  overflow-y: auto;
  width: 100%;
  padding: 5px;
}

.carta-evento {
  height: auto;
  min-height: 40px;
  margin-bottom: 2px;
  padding: 2px 5px;
  font-size: 16px;
  font-weight: 400;
  border-radius: 4px;
  color: white;
  display: flex;
  align-items: center;
}

.evento-descripcion {
  display: box;
  box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.2;
  max-height: 2.4em;
  white-space: normal;
}

.v-card-title {
  margin: 0;
  text-align: center;
}

.v-col {
  border: 1px solid #d3d3d3;
  flex: 1;
}

.redondeado {
  background-color: white;
  border-radius: 50%;
  width: 35px;
  height: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
}

.dia-fuera-mes {
  color: lightgray;
}
</style>
