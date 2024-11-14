<template>
    <v-dialog max-width="600px" v-model="mostrar" persistent>
      <v-card>
        <v-card-title class="d-flex justify-space-between align-center">
          <span class="headline">{{ tituloFormulario }}</span>
          <v-btn aria-label="cerrar" icon @click="cerrarDialogo" flat>
              <v-icon>mdi-close</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text>
            <div class="flex-item">
              <v-alert type="info" color="none">
                ¿Estás seguro de que quieres cargar todas las sesiones para el grupo seleccionado desde la fecha indicada?
              </v-alert>
            </div>
          <v-form ref="form" @submit.prevent="confirmarCarga">
            <div class="flex-container">
              <div class="flex-item">
                <v-select
                  v-model="grupoSeleccionado"
                  :items="grupos"
                  label="Selecciona un grupo"
                  item-title="nombre"
                  item-value="nombre"
                  :rules="[reglas.necesario]"
                  required
                ></v-select>
              </div>
              <div class="flex-item">
                <v-select
                  v-model="unidad"
                  :items="unidadesRegistradas"
                  label="Selecciona una unidad"
                  :rules="[reglas.necesario]"
                  required
                ></v-select>
              </div>
              <div class="flex-item">
                <v-text-field
                  v-model="fechaInicial"
                  label="Fecha inicial"
                  type="date"
                  :rules="[reglas.necesario]"
                  required
                ></v-text-field>
              </div>
            </div>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn aria-label="cancelar" class="rechazo" text @click="cerrarDialogo">Cancelar</v-btn>
              <v-btn aria-label="confirmar" class="claro" type="submit">Confirmar</v-btn>
            </v-card-actions>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  import unidades from "@/assets/unidades.json";
  export default {
    props: {
      grupos: {
        type: Array,
        required: true,
      },
    },
    data() {
      return {
        tituloFormulario: "Cargar Sesiones",
        mostrar: true,
        grupoSeleccionado: null,
        fechaInicial: "",
        reglas: {
          necesario: (value) => !!value || "Este campo es requerido",
        },
        unidad:"",
        unidadesRegistradas:unidades
      };
    },
    methods: {
      cerrarDialogo() {
        this.$emit("cerrar");
        this.resetForm();
      },
      resetForm() {
        this.grupoSeleccionado = null;
        this.fechaInicial = "";
      },
      async confirmarCarga() {
        let isValido = await this.$refs.form.validate();
        if (isValido.valid) {
          this.$emit("confirmar", {
            grupo: this.grupoSeleccionado,
            fecha: this.fechaInicial,
            unidad:this.unidad
          });
          this.cerrarDialogo();
        }
      },
    },
    created() {
      this.resetForm();
    },
  };
  </script>
  
  <style scoped>
  .headline {
    margin-top: 2vh;
  }
  
  .flex-container {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
  
  .flex-item {
    display: flex;
    flex-direction: column;
  }
  .v-alert{
    margin-bottom: 20px;
  }
  </style>
  