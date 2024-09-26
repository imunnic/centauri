<template>
  <v-dialog max-width="600px" v-model="mostrar">
    <v-card>
      <v-card-title class="d-flex justify-space-between">
        <span class="headline">{{ tituloFormulario }}</span>
        <v-btn icon @click="cerrarDialogo" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <v-form ref="form" @submit.prevent="guardarEquipamiento">
          <div class="flex-container">
            <div class="flex-item">
              <v-text-field v-model="equipamientoNuevo.nombre" label="Nombre" :rules="[reglas.necesario]"
                required></v-text-field>
            </div>
            <div class="flex-item">
              <v-text-field v-model="equipamientoNuevo.tipo" label="Tipo" :rules="[reglas.necesario]"
                required></v-text-field>
            </div>
            <div class="flex-item">
              <v-textarea v-model="equipamientoNuevo.descripcion" label="Descripción"></v-textarea>
            </div>
            <div class="flex-item">
              <v-text-field v-model="equipamientoNuevo.url" label="Enlace de Imagen" type="url"></v-text-field>
              <v-img v-if="equipamientoNuevo.url" :src="equipamientoNuevo.url" class="image-preview"
                max-height="200px"></v-img>
            </div>
          </div>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" text @click="cerrarDialogo">Cancelar</v-btn>
            <v-btn color="primary" type="submit">Guardar</v-btn>
          </v-card-actions>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: {
    equipamiento: {
      type: Object,
      default: () => ({})
    },
    edicion: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      tituloFormulario: this.edicion ? 'Editar Equipamiento' : 'Crear Equipamiento',
      mostrar: true,
      equipamientoNuevo: {},
      reglas: {
      necesario: value => !!value || 'Este campo es requerido',
    }
    };
  },
  watch: {
    edicion(nuevoValor) {
      this.tituloFormulario = nuevoValor ? 'Editar Equipamiento' : 'Crear Equipamiento';
    },
    equipamiento: {
      immediate: true,
      handler(nuevoValor) {
        this.equipamientoNuevo = { ...nuevoValor };
      }
    }
  },
  methods: {
    cerrarDialogo() {
      this.$emit('cerrar');
      this.resetForm();
    },
    resetForm() {
      this.equipamientoNuevo = { ...this.equipamiento };
    },
    async guardarEquipamiento() {
      let isValido = await this.$refs.form.validate();
      console.log(isValido);
      if (isValido.valid) {
        this.$emit('guardar', this.equipamientoNuevo);
        this.cerrarDialogo();
      }
    }
  },
  created() {
    this.resetForm();
  }
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

.image-preview {
  margin-top: 16px;
}
</style>