<template>
    <v-dialog v-model="dialog" persistent max-width="600px" width="fit-content">
      <v-card>
        <v-card-title class="flex-fila justify-space-between align-center">
          <span>Solicitar Acceso</span>
  
          <v-btn aria-label="cerrar" icon @click="cerrar" flat>
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <form @submit.prevent="submitForm" class="contenedor-flex">
            <div>
                <v-select
                  v-model="grupoSeleccionado"
                  :items="grupos"
                  item-title="nombre"
                  item-value="id"
                  label="Seleccione un grupo"
                  :rules="[reglas.required]"
                  required
                  class="item-flex input-medio"
                ></v-select>
            </div>
            <div class="botones flex-fila">
                <v-btn
                  aria-label="solicitar-acceso"
                  type="submit"
                  class="claro"
                >
                  Solicitar acceso
                </v-btn>
                <v-btn
                  aria-label="cancelar"
                  @click="cerrar"
                  class="claro"
                >
                  Cancelar
                </v-btn>
            </div>
          </form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  export default {
    props: {
      grupos: {
        type: Array,
        required: true,
      },
    },
    data() {
      return {
        dialog: true,
        grupoSeleccionado: null,
        reglas: {
          required: (value) => !!value || "Este campo es requerido",
        },
      };
    },
    methods: {
      cerrar() {
        this.dialog = false;
        this.$emit("cerrar");
      },
      submitForm() {
        if (this.grupoSeleccionado) {
          this.$emit("solicitar-acceso", this.grupoSeleccionado);
          this.cerrar();
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .contenedor-flex{
    flex-flow: column;
    align-items: start;
  }
  .item-flex{
    justify-self: flex-start;
    min-width: 300px;
  }
  .botones{
    align-self: flex-end;
  }
  .claro{
    margin: 10px;
  }
  </style>