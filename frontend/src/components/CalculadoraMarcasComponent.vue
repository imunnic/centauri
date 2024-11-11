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
              <v-btn :disabled="!mostrarVAM" @click="guardarVAM" class="rechazo"
                >Guardar</v-btn
              >
            </div>
          </v-tabs-window-item>

          <v-tabs-window-item value="rm">
            <h3>Calculadora RM</h3>
            <p label="Entrada RM" v-html="testRM"></p>
            <v-autocomplete v-model="ejercicio" :items="ejercicios"> </v-autocomplete>
            <v-text-field
              v-model="peso"
              type="number"
              class="input-medio"
              label="Peso"
              placeholder="60"
            ></v-text-field>
            <v-text-field
              v-model="repeticiones"
              type="number"
              class="input-medio"
              label="Repeticiones"
              placeholder="4"
            ></v-text-field>
            <div class="resultado">
              <b v-if="mostrarRM">Resultado: {{ resultadoRM }} kg</b>
              <v-btn :disabled="!ejercicio" @click="calcularRM" class="claro">Calcular</v-btn>
              <v-btn :disabled="!mostrarRM" @click="guardarRM" class="rechazo"
                >Guardar</v-btn
              >
            </div>
          </v-tabs-window-item>
        </v-tabs-window>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import { useEjerciciosStore } from "@/store/ejerciciosStore";
import { mapActions } from "pinia";
import configuracion from "@/configuracion.json";
export default {
  data() {
    return {
      dialog: true,
      tab: "vam",
      resultadoVAM: "",
      resultadoRM: 0,
      testCooper: configuracion.instruccionesCooper,
      testRM: configuracion.instruccionesRM,
      distancia: 1000,
      peso: 20,
      repeticiones: 4,
      mostrarVAM: false,
      mostrarRM: false,
      ejercicios: [],
      ejercicio:null
    };
  },
  methods: {
    ...mapActions(useEjerciciosStore, ["getEjerciciosPorCarga"]),
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
    guardarVAM() {
      let [minutos, segundos] = this.resultadoVAM.split(":").map(Number);
      let cantidad = minutos * 60 + segundos;
      this.$emit("guardar-VAM", cantidad);
    },
    calcularRM() {
      this.resultadoRM = Math.floor(
        this.peso / (1.0278 - 0.0278 * this.repeticiones)
      );
      this.mostrarRM = true;
    },
    guardarRM(){
      let marca = {
        nombre:this.ejercicio,
        cantidad:this.resultadoRM
      };
      this.$emit("guardar-RM", marca);
    },
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
  },
  async created() {
    let ejerciciosRM = await this.getEjerciciosPorCarga("RM");
    this.ejercicios = ejerciciosRM.map((ejercicio) => ejercicio.nombre);
  },
};
</script>

<style scoped>
.v-tabs-window-item {
  margin: 10px;
}
.resultado {
  display: flex;
  flex-flow: column;
  gap: 10px;
}
.resultado > .v-btn {
  width: fit-content;
}
</style>
