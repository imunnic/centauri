<template>
    <v-container>
      <v-form ref="form" v-model="valid" @submit.prevent="guardarFicha">
        <h2>Nueva ficha</h2>
        <div class="flex-container">
          <v-text-field label="Nombre de la ficha" v-model="ficha.nombre" :rules="nombreRules"
            class="input-field" required></v-text-field>
          <v-select :items="tiposFicha" label="Tipo de ficha" v-model="ficha.tipoFicha"
            :rules="tipoRules" class="input-field" required></v-select>
            <v-select :items="partesSesion" label="Parte de la sesión" v-model="ficha.parteSesion"
            :rules="parteRules" class="input-field" required></v-select>
        </div>
        <h3>Rutina</h3>
        <FichaComponent :rondas="ficha.rutina" @update-ficha="actualizarRutina" />
        <v-btn class="guardar-btn" type="submit">Guardar</v-btn>
      </v-form>
    </v-container>
  </template>
  
  <script>
  import FichaComponent from './FichaComponent.vue';
  
  export default {
    components: {
      FichaComponent
    },
    data() {
      return {
        valid: false,
        ficha: {
          nombre: '',
          tipoFicha: '',
          rutina: []
        },
        tiposFicha: ['RESISTENCIA', 'FUERZA'],
        partesSesion:['CALENTAMIENTO', 'FUNDAMENTAL', 'COORDINACION', 'CALMA'],
        nombreRules: [
          v => !!v || 'El nombre es requerido',
          v => v.length <= 50 || 'El nombre debe tener menos de 50 caracteres'
        ],
        tipoRules: [
          v => !!v || 'El tipo es requerido'
        ],
        parteRules: [
          v => !!v || 'La parte es requerida'
        ]
      };
    },
    methods: {
      actualizarRutina(rondas) {
        this.ficha.rutina = rondas;
      },
      guardarFicha() {
  
        console.log(this.ficha)
        if (this.$refs.form.validate()) {
          // Control de validación
          alert('La ficha ha sido guardada exitosamente');
        } else {
          alert('Formulario no válido, por favor revise los campos.');
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .flex-container {
    display: flex;
    gap: 10px;
    /* Espacio opcional entre los campos de entrada */
    margin-bottom: 20px;
    /* Espacio opcional entre los campos de entrada y los componentes */
  }
  
  .input-field {
    max-width: 300px;
  }
  
  .calentamiento,
  .rutina,
  .estiramiento {
    position: relative;
    padding: 10px;
    margin-bottom: 20px;
    /* Espacio opcional entre los componentes */
  }
  
  .calentamiento::before {
    border-radius: 5px 0px 0px 5px;
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 5px;
    background-color: var(--calentamiento);
  }
  
  .estiramiento::before {
    border-radius: 5px 0px 0px 5px;
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 5px;
    background-color: var(--estiramiento);
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
    /* Espacio opcional entre el último componente y el botón */
  }
  </style>