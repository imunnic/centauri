<template>
  <v-container>
    <v-form ref="formulario" v-model="valid" @submit.prevent="guardarFicha">
      <h2>Nueva ficha</h2>
      <div class="flex-container">
        <v-text-field
          label="Nombre de la ficha"
          v-model="fichaLocal.nombre"
          :rules="nombreRules"
          class="input-field placeholder"
          required
          placeholder="Ficha 1.1 - Calentamiento global"
        ></v-text-field>

        <v-select
          :items="tiposFicha"
          label="Tipo de ficha"
          v-model="fichaLocal.tipoFicha"
          :rules="tipoRules"
          class="input-field placeholder"
          required
          placeholder="Selecciona tipo de ficha"
        ></v-select>

        <v-select
          :items="partesSesion"
          label="Parte de la sesión"
          v-model="fichaLocal.parteSesion"
          :rules="parteRules"
          class="input-field placeholder"
          required
          item-title="nombre"
          item-value="valor"
          placeholder="Selecciona parte de la sesión"
        ></v-select>
      </div>

      <div class="flex-container">
        <div class="rpe-estimado">
          <v-text-field
            v-model="fichaLocal.rpeEstimado"
            class="input-corto placeholder"
            type="number"
            label="RPE estimado"
            hint="Esfuerzo previsto para la sesión"
            placeholder="Valor del 0 al 10"
            required
            :rules="rpeRules"
            min="1"
            max="10"
          ></v-text-field>
        </div>
        <div class="tiempo-estimado">
          <v-text-field
            class="input-corto placeholder"
            v-model="fichaLocal.tiempoEstimado"
            label="Tiempo"
            type="number"
            hint="Tiempo previsto en minutos"
          ></v-text-field>
        </div>
      </div>

      <h3>Rutina</h3>
      <FichaComponent
        :rondas="fichaLocal.rutina"
        @update-ficha="actualizarRutina"
      />
      <v-textarea
        v-model="fichaLocal.descripcion"
        label="Descripción"
        placeholder="Ficha de calentamiento con flexiones, abdominales, etc"
        hint="Introduce una descripción de la ficha"
        class="descripcion placeholder"
        rows="4"
        outlined
      ></v-textarea>
      <v-btn aria-label="guardar" class="guardar-btn claro" type="submit">Guardar</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import FichaComponent from "./FichaComponent.vue";
import { useFichasStore } from "@/store/fichasStore";
import { mapActions } from "pinia";

export default {
  components: {
    FichaComponent,
  },
  props: {
    ficha: {
      type: Object,
      default: () => ({
        nombre: "",
        string: "",
        rpeEstimado: 1,
        rutina: [],
        tiempoEstimado: 0,
        descripcion: "",
      }),
    },
  },
  data() {
    return {
      valid: false,
      fichaLocal: {
        nombre: "",
        string: "",
        rpeEstimado: 1,
        rutina: [],
        tiempoEstimado: 0,
        descripcion: "",
      },
      tiposFicha: [
        "Resistencia",
        "Fuerza",
        "Flexibilidad",
        "Movilidad",
        "Entrenamiento Total",
      ],
      partesSesion: [
        { nombre: "Calentamiento", valor: "CALENTAMIENTO" },
        { nombre: "Parte fundamental", valor: "FUNDAMENTAL" },
        { nombre: "Coordinación Funcional", valor: "COORDINACION" },
        { nombre: "Vuelta a la Calma", valor: "CALMA" }
      ],
      nombreRules: [
        (v) => !!v || "El nombre es requerido",
        (v) => v.length <= 50 || "El nombre debe tener menos de 50 caracteres",
      ],
      tipoRules: [(v) => !!v || "El tipo es requerido"],
      parteRules: [(v) => !!v || "La parte es requerida"],
      rpeRules: [
        (v) => (v > 0 && v <= 10) || "El valor del rpe debe estar entre 1 y 10",
      ],
    };
  },
  watch: {
    ficha: {
      immediate: false,
      handler(newFicha) {
        this.fichaLocal = { ...newFicha };
      },
    },
  },
  methods: {
    ...mapActions(useFichasStore, ["grabarFicha", "editarFicha"]),
    actualizarRutina(rondas) {
      this.fichaLocal.rutina = rondas;
    },
    async guardarFicha() {
      let isValido = await this.$refs.formulario.validate();
      if (this.$route.query.edicion == "true") {
        await this.editarFicha(this.fichaLocal, this.$route.params.id);
        this.$router.push({ path: "/fichas" });
      } else {
        if (isValido.valid) {
          await this.grabarFicha(this.fichaLocal);
          this.$router.push({ path: "/fichas" });
        }
      }
    },
  },
};
</script>

<style scoped>
.flex-container {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.input-corto {
  width: 300px;
  max-width: 200px;
}

.input-field {
  max-width: 300px;
}

.descripcion {
  margin-top: 20px;
}

.rutina {
  position: relative;
  padding: 10px;
  margin-bottom: 20px;
}

.rutina::before {
  border-radius: 5px 0px 0px 5px;
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 5px;
  background-color: var(--claro);
}

.guardar-btn {
  display: block;
  margin-left: auto;
  margin-top: 20px;
}
</style>
