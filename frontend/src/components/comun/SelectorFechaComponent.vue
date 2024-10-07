<!--
  Este componente actúa como un selector de fechas y modos de visualización. Está diseñado para permitir 
  la navegación entre fechas y la selección de un modo de visualización (día o mes) con botones de control 
  y un desplegable. Se apoya en la librería Vuetify (https://vuetifyjs.com/) para su estructura y estilo.

  Props:
  - fecha: Objeto de tipo Date que representa la fecha actual mostrada en el componente. Es requerido.

  Eventos:
  - cambiar-fecha: Se emite cuando se pulsa en los botones de flecha para avanzar o retroceder en la fecha, 
    pasando un valor de `-1` o `+1` según la dirección.
  - cambiar-modo: Se emite cuando se selecciona un nuevo modo ('dia' o 'mes') en el desplegable.
-->

<template>
  <v-container>
    <div class="contenedor-flex">
      <v-btn aria-label="anterior-fecha" @click="$emit('cambiar-fecha', -1)" icon flat>
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>

      <div class="contenedor-texto">
        <v-toolbar-title><b>{{ fechaMostrada }}</b></v-toolbar-title>
      </div>

      <v-btn aria-label="siguiente-fecha" @click="$emit('cambiar-fecha', 1)" icon flat>
        <v-icon>mdi-chevron-right</v-icon>
      </v-btn>

      <v-select
        class="selector placeholder"
        placeholder="Modo"
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
