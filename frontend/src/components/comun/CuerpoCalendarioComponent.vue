<!--
  Este componente muestra un calendario en formato de mes y permite seleccionar una fecha. Además, muestra las sesiones del día y emite eventos cuando se selecciona una fecha o una sesión.

  Props:
  - fecha: La fecha actualmente seleccionada que se muestra en el calendario.
  - sesiones: Un array de sesiones que se muestran en el calendario. Cada sesión tiene una fecha, un nombre, y un grupo con su color asociado.

  Eventos:
  - fecha-seleccionada: Se emite cuando el usuario selecciona una fecha, entregando la fecha completa como payload.
  - sesion-seleccionada: Se emite cuando el usuario selecciona una sesión específica, entregando el objeto de la sesión como payload.
-->

<template>
  <v-container fluid>
    <v-row class="no-gutters"> <!-- https://vuetifyjs.com/en/components/grids/#justify -->
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
          <v-card-title
            class="titulo-dia"
            :class="{
              claro:
                dia.fecha &&
                dia.fecha === fechaActual.dia &&
                fecha.getMonth() === fechaActual.mes &&
                fecha.getFullYear() === fechaActual.año &&
                !dia.esDeOtroMes,
              redondeado:
                dia.fecha &&
                dia.fecha === fechaActual.dia &&
                fecha.getMonth() === fechaActual.mes &&
                fecha.getFullYear() === fechaActual.año,
              'dia-fuera-mes': dia.esDeOtroMes,
            }"
          >
            <span>{{ dia.fecha }}</span>
          </v-card-title>

          <v-container class="lista-eventos" v-if="sesionesPorDia(dia).length">
            <v-card
              v-for="(sesion, sesionIndex) in sesionesPorDia(dia)"
              :key="sesionIndex"
              :style="{
                backgroundColor: sesion.grupo.color + ' !important',
              }"
              class="carta-evento claro"
              flat
              @click.stop="emitirEvento(sesion)"
            >
              <p class="evento-descripcion">{{ sesion.nombre }} - {{ sesion.grupo.nombre }}</p>
            </v-card>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  props: {
    fecha: {
      type: Date,
      required: true,
    },
    sesiones: {
      type: Array,
      required: true,
      default: [],
    },
  },
  computed: {
    fechaActual() {
      const hoy = new Date();
      return {
        dia: hoy.getDate(),
        mes: hoy.getMonth(),
        año: hoy.getFullYear(),
      };
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
    };
  },
  watch: {
    fecha: {
      immediate: true,
      handler() {
        this.generarCalendario();
      },
    },
    sesiones: {
      handler() {
        this.generarCalendario();
      },
      deep: true,
    },
  },
  methods: {
    generarCalendario() {
      const mesActual = this.fecha.getMonth();
      const añoActual = this.fecha.getFullYear();

      let primerDiaDelMes = new Date(añoActual, mesActual, 1);
      let diaDeLaSemana = primerDiaDelMes.getDay();

      if (diaDeLaSemana === 0) {
        diaDeLaSemana = 7;
      }
      primerDiaDelMes.setDate(primerDiaDelMes.getDate() - (diaDeLaSemana - 1));

      const diasDelMes = [];
      for (let dia = 0; dia < 42; dia++) {
        const fecha = new Date(primerDiaDelMes);
        fecha.setDate(primerDiaDelMes.getDate() + dia);
        diasDelMes.push({
          fecha: fecha.getDate(),
          fechaCompleta: fecha,
          esDeOtroMes: fecha.getMonth() !== mesActual,
        });
      }

      this.semanasDelMes = [];
      for (let i = 0; i < diasDelMes.length; i += 7) {
        this.semanasDelMes.push(diasDelMes.slice(i, i + 7));
      }
    },

    sesionesPorDia(dia) {
      if (!dia || !dia.fechaCompleta) return [];

      const diaFechaCompleta = dia.fechaCompleta;

      const fechaFormateada = `${diaFechaCompleta
        .getDate()
        .toString()
        .padStart(2, "0")}/${(diaFechaCompleta.getMonth() + 1)
        .toString()
        .padStart(2, "0")}/${diaFechaCompleta.getFullYear()}`;

      return this.sesiones.filter((sesion) => sesion.fecha === fechaFormateada);
    },

    emitirEvento(sesion) {
      this.$emit("sesion-seleccionada", sesion);
    },

    emitirFecha(dia) {
      if (dia.fechaCompleta) {
        this.$emit("fecha-seleccionada", dia.fechaCompleta);
      }
    },
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
