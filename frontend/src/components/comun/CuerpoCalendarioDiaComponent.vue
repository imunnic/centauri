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
  <v-container fluid>
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
        <template v-if="sesionesDelDia.length">
          <v-card
            v-for="(sesion, sesionIndex) in sesionesDelDia"
            :key="sesionIndex"
            class="carta-eventos claro"
            flat
            @click.stop="emitirEvento(sesion)"
            :style="{
              backgroundColor: sesion.grupo.color.valor + ' !important',
            }"
          >
            {{ sesion.nombre }} - {{ sesion.grupo.nombre }}
          </v-card>
        </template>
        <template v-else>
          <v-card class="carta-eventos sin-sesiones" flat>
            No hay sesiones previstas para esta fecha
          </v-card>
        </template>
      </v-container>
    </v-card>
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
    dia() {
      const dia = this.fecha.getDate();
      return {
        fecha: dia,
        fechaCompleta: this.fecha,
      };
    },
    sesionesDelDia() {
      if (!this.dia.fecha) return [];
      if (!Array.isArray(this.sesiones)) {
        console.error("sesiones no es un array");
        return [];
      }
      const fechaFormateada = `${this.dia.fecha.toString().padStart(2, "0")}/${(
        this.fecha.getMonth() + 1
      )
        .toString()
        .padStart(2, "0")}/${this.fecha.getFullYear()}`;
      return this.sesiones.filter((sesion) => sesion.fecha === fechaFormateada);
    },
  },
  methods: {
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
.carta-dia {
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  min-height: 400px;
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
  overflow-y: auto;
  width: 100%;
  height: 100%;
  padding: 5px;
}

.carta-eventos {
  margin-bottom: 2px;
  padding: 2px 5px;
  font-size: 20px;
  border-radius: 4px;
}

.v-card-title {
  margin: 0;
  text-align: center;
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

.sin-sesiones {
  text-align: center;
  color: #555;
  font-size: 20px;
}
</style>
