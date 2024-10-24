<template>
  <v-form ref="form" @submit.prevent="" class="formulario">
    <h3>Notas de la sesión</h3>
    <b>RPE:</b>
    <div class="input-con-icono">
      <ValoracionComponent @valor-seleccionado="ajustarRpe" />
      <InformacionComponent :texto="ayudaRpe" />
    </div>
    <b>Tiempo:</b>
    <div class="input-con-icono">
      <v-text-field
        class="placeholder input-corto"
        placeholder="35"
        v-model="tiempo"
        type="number"
        label="Tiempo"
        :rules="[
          (v) => !!v || 'El tiempo de la sesión es necesario',
          (v) => v >= 1 || 'El tiempo debe ser superior a 1',
        ]"
        :min="1"
        required
      />
      <InformacionComponent :texto="ayudaTiempo" />
    </div>
    <b>Comentarios:</b>
    <v-textarea
      class="placeholder"
      placeholder="En esta sesión realicé 3 repeticiones del circuito"
      v-model="comentarios"
      label="Comentarios"
    />
    <div class="contenedor-flex botones">
      <v-btn
        aria-label="realizar-sesion"
        class="claro boton"
        @click="sesionRealizada"
        >Hecha</v-btn
      >
      <v-btn 
        aria-label="salir-sesion" 
        class="claro boton" 
        @click="salir"
        >Salir</v-btn
      >
    </div>
  </v-form>
</template>

<script>
import InformacionComponent from "@/components/comun/InformacionComponent.vue";
import ValoracionComponent from "@/components/comun/ValoracionComponent.vue";
import tooltips from "@/tooltips.json";

export default {
  props: {
    tiempoFinal: {
      type: Number,
      required: false,
    },
  },
  components: { InformacionComponent, ValoracionComponent },
  data() {
    return {
      ayudaRpe: tooltips.ayudaRPE,
      ayudaTiempo: tooltips.ayudaTiempo,
      rpe: 6,
      tiempo: 10,
      comentarios: "",
    };
  },
  methods: {
    ajustarRpe(valor) {
      this.rpe = valor * 2;
    },
    async sesionRealizada() {
      let valido = await this.$refs.form.validate();
      if (valido.valid) {
        let sesionRealizada = {
          rpe: this.rpe,
          tiempo: this.tiempo,
          comentarios: this.comentarios,
        };
        this.$emit("sesion-realizada", sesionRealizada);
      }
    },
    salir() {
      this.$emit("salir");
    },
  },
  created() {
    if (this.tiempoFinal) {
      this.tiempo = this.tiempoFinal;
    }
  },
};
</script>

<style scoped>
.botones {
  justify-content: end;
  align-content: center;
}
.boton {
  margin-left: 10px;
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
