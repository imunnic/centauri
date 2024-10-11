<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
      <v-card>
        <v-card-title class="flex-fila justify-space-between">
          <span v-if="edicion">Editar Grupo</span>
          <span v-else>Crear Nuevo Grupo</span>
  
          <v-btn aria-label="cerrar" icon @click="cerrar" flat>
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <form @submit.prevent="submitForm" class="contenedor-flex">
            <v-text-field
              label="Nombre del grupo"
              v-model="nombre"
              :rules="[reglas.required]"
              required
              placeholder="Ingrese el nombre del grupo"
              class="item-flex placeholder"
            ></v-text-field>
            Seleccione color
            <v-color-picker
              v-model="color"
              flat
              hide-inputs
              hide-canvas
              hide-sliders
              show-swatches
              :swatches="swatches"
              :dot-size="20"
              :width="300"
              class="item-flex placeholder"
            ></v-color-picker>
  
            <v-btn
              aria-label="crear-grupo"
              v-if="!edicion"
              type="submit"
              class="claro"
              >Crear Grupo</v-btn
            >
            <v-btn aria-label="editar-grupo" v-else type="submit" class="claro"
              >Editar Grupo</v-btn
            >
          </form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  import configuracion from "@/configuracion.json"
  export default {
    props: {
      grupo: {
        type: Object,
        required: false,
      },
      edicion: {
        type: Boolean,
        default:false,
        required: false,
      },
    },
    data() {
      return {
        dialog: true, 
        nombre: this.grupo?.nombre || "", 
        color: this.grupo?.color || "#000000",
        reglas: {
          required: (value) => !!value || "Este campo es requerido",
        },
        swatches: configuracion.colores
      };
    },
    methods: {
      cerrar() {
        this.dialog = false;
        this.$emit("cerrar");
      },
      submitForm() {
        const nuevoGrupo = {
          nombre: this.nombre,
          color: this.color,
        };
        this.$emit('crear-grupo',nuevoGrupo)
        this.resetForm();
        this.cerrar();
      },
      resetForm() {
        this.nombre = "";
        this.color = "#000000";
      },
    },
    created() {
      if (this.edicion && this.grupo) {
        this.nombre = this.grupo.nombre;
        this.color = this.grupo.color;
      }
    },
  };
  </script>
  
  <style scoped>
  .contenedor-flex {
    display: flex;
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  .item-flex {
    display: flex;
    flex-direction: column;
  }
  </style>
  