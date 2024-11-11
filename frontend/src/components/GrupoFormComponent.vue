<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="flex-fila justify-space-between align-center">
        <span v-if="edicion">Editar Grupo</span>
        <span v-else>Crear Nuevo Grupo</span>

        <v-btn aria-label="cerrar" icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <v-form ref="formulario" @submit.prevent="submitForm" class="contenedor-flex">
          <v-text-field
            label="Nombre del grupo"
            v-model="nombre"
            :rules="[reglas.required, reglas.existe]"
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
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import configuracion from "@/configuracion.json";
import { useGruposStore } from "@/store/gruposStore.js";
import {useAlertasStore} from "@/store/alertasStore.js";
import { mapActions } from "pinia";
export default {
  props: {
    grupo: {
      type: Object,
      required: false,
    },
    edicion: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      dialog: true,
      nombre: this.grupo?.nombre || "",
      color: this.grupo?.color || "#000000",
      existe: false,
      reglas: {
      required: (value) => !!value || "Este campo es requerido",
      existe: () => !this.existe || "Este nombre ya existe",
      },
      swatches: configuracion.colores,
    };
  },
  methods: {
    ...mapActions(useGruposStore, ["existeGrupo"]),
    ...mapActions(useAlertasStore, ['mostrarError']),
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
    async submitForm() {
      try {
        let isValido = await this.$refs.formulario.validate();
        if (isValido.valid){
          const nuevoGrupo = {
            nombre: this.nombre,
            color: this.color,
          };
          this.$emit("crear-grupo", nuevoGrupo);
          this.resetForm();
          this.cerrar();
        }
      } catch (error) {
        this.mostrarError("No se ha podido validar el formulario, revise los datos")
      }
    },
    async verificarExiste() {
      try {
        let response = await this.existeGrupo(this.nombre);
        this.existe = response;
      } catch (error) {
        this.mostrarError("Ese grupo no existe");
      }
    },
    resetForm() {
      this.nombre = "";
      this.color = "#000000";
      this.existe = false;
    },
  },
  watch: {
    async nombre(nuevoValor){
      await this.verificarExiste(nuevoValor);
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
