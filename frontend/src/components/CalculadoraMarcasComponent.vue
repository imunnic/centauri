<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <span>Calculadora</span>
        <v-btn aria-label="cerrar" icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-tabs v-model="tab">
        <v-tab value="vam">Calculadora VAM</v-tab>
        <v-tab value="rm">Calculadora RM</v-tab>
      </v-tabs>

      <v-card-text>
        <v-tabs-window v-model="tab">
          <v-tabs-window-item value="vam">
            <h3>Calculadora VAM</h3>
            <p label="Entrada VAM" v-html="testCooper"></p>
            <v-text-field
              type="number"
              class="input-medio"
              label="Resultado en m"
              placeholder="1700"
              v-model="distancia"
            ></v-text-field>
            <div class="resultado">
              <b v-if="mostrarVAM">Resultado: {{ resultadoVAM }} min/km</b>
              <v-btn @click="calcularVAM" class="claro">Calcular</v-btn>
              <v-btn :disabled="!mostrarVAM" @click="guardarVAM" class="rechazo">Guardar</v-btn>
            </div>
          </v-tabs-window-item>

          <v-tabs-window-item value="rm">
            <h3>Calculadora RM</h3>
            <p label="Entrada RM" v-html="testRM"></p>
            <v-text-field
              type="number"
              class="input-medio"
              label="Resultado"
              placeholder="1700"
            ></v-text-field>

            <v-btn @click="calcularRM">Calcular</v-btn>
          </v-tabs-window-item>
        </v-tabs-window>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import configuracion from "@/configuracion.json";
export default {
  data() {
    return {
      dialog: true,
      tab: "vam",
      resultadoVAM: "",
      resultadoRM: "",
      testCooper: configuracion.instruccionesCooper,
      testRM: configuracion.instruccionesRM,
      distancia: 1000,
      mostrarVAM: false,
    };
  },
  methods: {
    calcularVAM() {

      let resultadoDecimal = 1 / (this.distancia / (100 * 60));
      let minutos = Math.floor(resultadoDecimal);
      let segundos = Math.round((resultadoDecimal - minutos) * 60);
      if (segundos < 10) {
        segundos = "0" + segundos;
      }
      this.resultadoVAM = `${minutos}:${segundos}`;
      this.mostrarVAM = true;
    },
    guardarVAM(){
      let [minutos, segundos] = this.resultadoVAM.split(':').map(Number);
      let cantidad = (minutos * 60) + segundos;
      this.$emit('guardar-VAM',cantidad);
    },
    calcularRM() {
      // Lógica de cálculo para RM
      this.resultadoRM = `Resultado de RM basado en: ${this.rmInput}`;
    },
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
  },
};
</script>

<style scoped>
.v-tabs-window-item {
  margin: 10px;
}
.resultado{
  display: flex;
  flex-flow: column;
  gap: 10px;
}
.resultado > .v-btn{
  width: fit-content;
}
</style>
