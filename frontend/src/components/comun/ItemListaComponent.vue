<!--
  Este componente está pensado para se uno de los elementos de una lista que se muestre en un 
  componente padre. Está apoyado en la librería de Vuetify(https://vuetifyjs.com/).

  Props:
  - item: Objeto que representa el ítem a mostrar en la tarjeta. Debe contener al menos las 
    propiedades `nombre` y `descripcion`.
  - acciones: Array de objetos que define las acciones disponibles para el ítem. Cada objeto 
    en el array debe tener las propiedades `icon` (nombre del ícono), `color` (color del botón) 
    y `evento` (nombre del evento que se emitirá cuando se haga clic en el botón). Por defecto, 
    se incluyen acciones para editar (ícono 'mdi-pencil') y eliminar (ícono 'mdi-trash-can').

-->
<template>
  <v-card class="carta" flat>
    <div class="contenedor-flex">
      <div v-if="mostrarImagen" class="imagen-contenedor">
        <v-img 
          :src="item.url || imagenPredeterminada" 
          :alt="item.url ? 'Imagen de ' + item.nombre : 'No hay imagen disponible'"
          contain
          class="imagen"
        />
      </div>
      <div class="contenido">
        <v-card-title class="titulo">{{ item.nombre }}</v-card-title>
        <v-card-text>
          {{ item.descripcion }}
          <slot name="info-extra" :item="item"></slot>
        </v-card-text>
        <v-card-actions v-if="acciones.length > 0">
          <v-btn 
            v-for="accion in acciones" 
            :key="accion.icon" 
            :icon="accion.icon" 
            :color="accion.color" 
            @click="emitAccion(accion.evento, item, $event)">
            <v-icon>{{ accion.icon }}</v-icon>
          </v-btn>
        </v-card-actions>
      </div>
    </div>
  </v-card>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      required: true
    },
    acciones: {
      type: Array,
      default: () => ([
        { icon: 'mdi-pencil', color: 'default', evento: 'editar' },
        { icon: 'mdi-trash-can', color: 'error', evento: 'eliminar' }
      ])
    },
    mostrarImagen: {
      type: Boolean,
      default: false
    },
    imagenPredeterminada: {
      type: String,
      default: '/no-imagen.png'
    }
  },
  methods: {
    emitAccion(evento, item, event) {
      event.stopPropagation();
      this.$emit(evento, item);
    }
  },
};
</script>

<style scoped>
.carta {
  margin-bottom: 16px;
}

.imagen-contenedor {
  margin-left: 16px;
  margin-right: 16px;
}

.imagen {
  border-radius: 50%;
  object-fit: cover;
  width: 100px;
  height: 100px;
}

.contenido {
  flex-grow: 1;
  overflow: hidden; 
}

.titulo{
  overflow: hidden;
  text-overflow: ellipsis; 
  max-width: 100%; 
}
</style>
