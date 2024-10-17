<!--
  Este componente `ItemListaComponent` representa un elemento en una lista, diseñado para ser utilizado dentro de un componente padre. Está construido con Vuetify (https://vuetifyjs.com/), permitiendo mostrar información detallada y realizar acciones específicas sobre cada ítem.

  Props:
  - item: Objeto que representa el ítem mostrado en la tarjeta. Las propiedades que puede tener son:
      - `nombre`: El nombre del ítem.
      - `descripcion(opcional)`: Una breve descripción del ítem.
      - `url`: (opcional) URL de la imagen asociada al ítem.
      - `color`: (opcional) Color de fondo para la tarjeta.
  - acciones: Array de objetos que define las acciones disponibles para el ítem. Cada objeto debe contener:
      - `icon`: El nombre del icono (de Material Design Icons).
      - `color`: El color del botón de acción.
      - `evento`: El evento que se emitirá al hacer clic en el botón.
    Por defecto, se incluyen dos acciones: editar (ícono 'mdi-pencil') y eliminar (ícono 'mdi-trash-can').
  - descripcion: Booleano que indica si se debe mostrar o no la descripción del ítem. El valor predeterminado es `true`.
  - mostrarImagen: Booleano que indica si se debe mostrar la imagen asociada al ítem. El valor predeterminado es `false`.
  - imagenPredeterminada: String que especifica la URL de una imagen predeterminada que se mostrará si el ítem no tiene una imagen propia. El valor predeterminado es '/no-imagen.png'.

  Slots:
  - info-extra: Un slot opcional que permite inyectar contenido extra debajo de la descripción del ítem.

  Eventos:
  - editar: Se emite cuando se hace clic en el botón de editar.
  - eliminar: Se emite cuando se hace clic en el botón de eliminar.
  (Estos son los eventos predeterminados, pero pueden variar si se modifican las acciones).
-->

<template>
  <v-card class="carta" :style="{ '--color-background': item.color || 'var(--claro)' }" flat>
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
        <div class="titulo-acciones">
          <v-card-title class="titulo">
            <slot name="titulo" :item="item">
              {{ item.nombre }}
            </slot>
          </v-card-title>
          <v-card-actions class="acciones" v-if="acciones.length > 0">
            <v-btn 
              v-for="accion in acciones" 
              :id="accion.evento"
              :key="accion.icon" 
              :icon="accion.icon" 
              :color="accion.color" 
              @click="emitAccion(accion.evento, item, $event)">
              <v-icon>{{ accion.icon }}</v-icon>
            </v-btn>
          </v-card-actions>
        </div>
        <v-card-text class="altura-ajustable">
          <p class="texto descripcion" v-if="descripcion">{{ item.descripcion }}</p>
          <slot name="info-extra" :item="item"></slot>
        </v-card-text>
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
    descripcion:{
      default:true
    },
    acciones: {
      type: Array,
      default: () => ([
        { icon: 'mdi-pencil', color: 'var(--claro)', evento: 'editar' },
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
  created(){
    console.log(this.item)
  }
};
</script>

<style scoped>
.carta {
  margin-bottom: 16px;
}
.carta::before {
  background-color: var(--color-background);
}

.imagen-contenedor {
  margin-left: 16px;
  margin-right: 16px;
}
.altura-ajustable{
  margin: 0px;
  padding: 0px;
  height: fit-content;
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

.titulo-acciones {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.titulo{
  white-space: normal;
  overflow: visible;
  word-wrap: break-word;
  max-width: 100%; 
}

.descripcion{
  padding-left: 15px;
}

.acciones {
  display: flex;
  gap: 8px;
  flex-grow: 1; 
  justify-content: flex-end; 
}

@media (max-width: 600px) {
  .titulo-acciones {
    flex-direction: column;
    align-items: flex-start;
  }

  .acciones {
    justify-content: flex-start;
    margin-top: 8px;
  }
}
</style>