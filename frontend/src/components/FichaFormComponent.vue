<template>
  <v-container>
    <v-form ref="formulario" v-model="valid" @submit.prevent="guardarFicha">
      <h2>Nueva ficha</h2>
      <div class="flex-container">
        <v-text-field
          label="Nombre de la ficha"
          v-model="ficha.nombre"
          :rules="nombreRules"
          class="input-field"
          required
          placeholder="Ficha 1.1"
        ></v-text-field>
        <v-select
          :items="tiposFicha"
          label="Tipo de ficha"
          v-model="ficha.tipoFicha"
          :rules="tipoRules"
          class="input-field"
          required
          placeholder="Fuerza"
        ></v-select>
        <v-select
          :items="partesSesion"
          label="Parte de la sesión"
          v-model="ficha.parteSesion"
          :rules="parteRules"
          class="input-field"
          required
          placeholder="Calentamiento"
        ></v-select>
      </div>
      <div class="flex-container">
        <div class="rpe-estimado">
          <v-text-field
            v-model="ficha.rpeEstimado"
            class="input-corto"
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
            class="input-corto"
            v-model="formatoTiempo"
            label="Tiempo"
            type="time"
            hint="Tiempo previsto para la sesión hh:mm:ss"
            step="2"
          ></v-text-field>
        </div>
      </div>
      <h3>Rutina</h3>
      <FichaComponent :rondas="ficha.rutina" @update-ficha="actualizarRutina" />
      <v-textarea
        v-model="ficha.descripcion"
        label="Descripción"
        placeholder="Ficha de calentamiento con flexiones, abdominales, etc"
        hint="Introduce una descripción de la ficha"
        class="descripcion"
        rows="4"
        outlined
      ></v-textarea>
      <v-btn class="guardar-btn" type="submit">Guardar</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import FichaComponent from './FichaComponent.vue';
import { useFichasStore } from '@/store/fichasStore';
import { mapActions } from 'pinia';

export default {
  components: {
    FichaComponent,
  },
  data() {
    return {
      valid: false,
      ficha: {
        nombre: '',
        tipoFicha: '',
        rpeEstimado: 1,
        rutina: [],
        tiempoEstimado: 0,
        descripcion: '',
      },
      tiposFicha: ['RESISTENCIA', 'FUERZA'],
      partesSesion: ['CALENTAMIENTO', 'FUNDAMENTAL', 'COORDINACION', 'CALMA'],
      nombreRules: [
        v => !!v || 'El nombre es requerido',
        v => v.length <= 50 || 'El nombre debe tener menos de 50 caracteres',
      ],
      tipoRules: [v => !!v || 'El tipo es requerido'],
      parteRules: [v => !!v || 'La parte es requerida'],
      rpeRules: [v => v > 0 && v <= 10 || 'El valor del rpe debe estar entre 1 y 10'],
    };
  },
  computed: {
    formatoTiempo: {
      get() {
        const minutes = Math.floor(this.ficha.tiempoEstimado / 60)
          .toString()
          .padStart(2, '0');
        const seconds = (this.ficha.tiempoEstimado % 60)
          .toString()
          .padStart(2, '0');
        return `${minutes}:${seconds}`;
      },
      set(value) {
        const [minutes, seconds] = value.split(':').map(Number);
        this.tiempoEstimado = minutes * 60 + seconds;
      },
    },
  },
  methods: {
    ...mapActions(useFichasStore, ['grabarFicha']),
    actualizarRutina(rondas) {
      this.ficha.rutina = rondas;
    },
    async guardarFicha() {
      let isValido = await this.$refs.formulario.validate();
      console.log(isValido.valid);
      if (isValido.valid) {
        console.log(this.ficha);
        await this.grabarFicha(this.ficha);
        this.$router.push({ path: '/fichas' });
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
  content: '';
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
