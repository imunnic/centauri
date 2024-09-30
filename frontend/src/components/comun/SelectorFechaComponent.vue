<template>
  <v-container>
    <div class="contenedor-flex">
      <v-btn @click="$emit('cambiar-fecha', -1)" icon flat>
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>

      <div class="contenedor-texto">
        <v-toolbar-title><b>{{ fechaMostrada }}</b></v-toolbar-title>
      </div>

      <v-btn @click="$emit('cambiar-fecha', 1)" icon flat>
        <v-icon>mdi-chevron-right</v-icon>
      </v-btn>

      <v-select
        class="selector"
        v-model="modo"
        :items="modos"
        item-title="texto"
        item-value="valor"
      />
    </div>
  </v-container>
</template>

<script>
export default {
  props: {
    fecha: {
      type: Date,
      required: true
    }
  },
  computed: {
    fechaMostrada() {
      if (this.modo === 'dia') {
        const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        return this.mayusculaPrimero(this.fecha.toLocaleDateString('es-ES', opciones));
      } else if (this.modo === 'mes') {
        const opciones = { year: 'numeric', month: 'long' };
        return this.mayusculaPrimero(this.fecha.toLocaleDateString('es-ES', opciones));
      }
    }
  },
  data() {
    return {
      modo: 'mes',
      modos: [
        { valor: 'dia', texto: 'Día' },
        { valor: 'mes', texto: 'Mes' }
      ]
    };
  },
  methods: {
    mayusculaPrimero(texto) {
      return texto.charAt(0).toUpperCase() + texto.slice(1);
    }
  },
  watch: {
    modo(nuevoModo) {
      this.$emit('cambiar-modo', nuevoModo);
    }
  }
};
</script>

<style scoped>
.contenedor-flex {
  display: flex;
  align-items: center;
  justify-content: center;
}

.contenedor-texto {
  flex: 1;
  display: flex;
  text-align: center;
}

.v-toolbar-title {
  font-size: 18px;
  margin: 0 16px;
}

.v-btn {
  margin: 0 8px;
}

.selector {
  min-width: 120px;
  max-width: 150px;
}
</style>
