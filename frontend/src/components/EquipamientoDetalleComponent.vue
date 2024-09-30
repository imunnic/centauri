<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="d-flex justify-space-between">
        <span>{{ equipamiento.nombre }}</span>
        <v-btn icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-subtitle></v-card-subtitle>
      <v-card-text>
        <v-img
          v-if="equipamiento.url"
          :src="equipamiento.url || urlImagenNoEncontrada"
          contain
          max-height="300px"
        />
        <p v-if="equipamiento.tipo"><b>Tipo</b>: {{ equipamiento.tipo }}</p>
        <p v-if="equipamiento.descripcion">
          <b>Descripción</b>: {{ equipamiento.descripcion }}
        </p>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import configuracion from "@/configuracion.json";
export default {
  props: {
    equipamiento: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      dialog: true,
      urlImagenNoEncontrada: configuracion.urlImagenNoEncontrada,
    };
  },
  methods: {
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
  },
};
</script>

<style scoped>
.texto {
  margin: 10px;
}
</style>
