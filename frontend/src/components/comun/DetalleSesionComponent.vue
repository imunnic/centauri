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
  <v-card class="pa-3">
    <v-card-title class="d-flex justify-space-between align-center">
      <span class="titulo-sesion">
        {{ sesion.nombre || "Sin nombre" }}
      </span>
      <v-icon @click="cerrarTarjeta" class="icono-cerrar"> mdi-close </v-icon>
    </v-card-title>

    <v-card-text>
      <p>{{ sesion.grupo.nombre }}</p>
      <v-list dense>
        <v-list-item v-for="(ficha, index) in sesion.fichas || []" :key="index">
          <v-card class="mb-2 claro" outlined>
            <v-card-title>{{ ficha.nombre }}</v-card-title>
          </v-card>
        </v-list-item>
      </v-list>
    </v-card-text>

    <v-card-actions>
      <v-btn color="primary" @click="cerrarTarjeta">Cerrar</v-btn>
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
  },
  methods: {
    cerrarTarjeta() {
      this.$emit("cerrar-tarjeta");
    },
  },
};
</script>

<style scoped>
.titulo-sesion {
  font-weight: bold;
  text-decoration: underline;
}

.icono-cerrar {
  cursor: pointer;
  color: gray;
}

v-list-item v-card {
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}
</style>
