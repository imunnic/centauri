<!--
  Este componente actúa integra varios subcomponentes de forma que se muestre un elemento que
  permita la búsqueda y visualización de cualquier listado de elementos, así como el poder
  realizar distintas acciones sobre ellos. Se apoya en la librería de Vuetify (https://vuetifyjs.com/).

  Componentes:
  - BuscadorComponent: Componente de búsqueda que permite filtrar los ítems de la lista. Recibe 
    las propiedades `items` y `filtros`, y emite un evento `filtrada` con los ítems filtrados. Al 
    buscador se le pasa un listado con todas las propiedades de los objetos del listado para buscar
    por ellas.
  - ItemListaComponent: Componente que representa cada ítem en la lista. Recibe la propiedad 
    `item` y un array de `acciones` para cada ítem, y emite eventos para manejar clics, ediciones, 
    y eliminaciones. También permite la inserción de contenido adicional a través del slot `info-extra`.
  - FabBotonComponent: Botón flotante que aparece en la esquina inferior derecha de la pantalla. 
    Se muestra solo si la propiedad `permisoCreacion` es `true` y emite un evento `click` para 
    crear un nuevo ítem.

  Props:
  - items: Array de ítems que se mostrarán en la lista. Debe ser proporcionado por el componente 
    padre.
  - cargando: Booleano que indica si los datos están siendo cargados. Si es `true`, muestra un 
    spinner de carga.
  - permisoCreacion: Booleano que determina si el botón flotante de acción está habilitado. Por 
    defecto es `true`.

  Data:
  - itemsFiltrados: Array que contiene los ítems después de aplicar los filtros.
  - filtrosCompletos: Array de claves de los ítems que se usan para los filtros.

  Computed:
  - acciones: Devuelve un array de acciones disponibles para los ítems basadas en el permiso de 
    creación.

-->
<template>
  <v-container>
    <div v-if="cargando" class="circulo-carga">
      <v-progress-circular :size="70" :width="7" indeterminate color="primary"></v-progress-circular>
    </div>

    <template v-else>
      <BuscadorComponent class="componente" :items="items" :filtros="filtrosCompletos"
        @filtrada="updateItemsFiltrados" />

      <ItemListaComponent class="componente" v-for="(item, index) in itemsFiltrados" :key="index" :item="item"
        :acciones="acciones"  :mostrar-imagen="mostrarImagen" :imagenPredeterminada="imagenPredeterminada" 
        @click="abrirDialogoDetalle(item)" @editar="emitEditar(item)" @eliminar="emitEliminar(item)">
        <template v-slot:info-extra="{ item }">
          <slot name="info-extra" :item="item"></slot>
        </template>
      </ItemListaComponent>

      <FabBotonComponent v-if="permisoCreacion" @click="emitCrear('crear')" />
    </template>
  </v-container>
</template>

<script>
import BuscadorComponent from '@/components/comun/BuscadorComponent.vue';
import ItemListaComponent from '@/components/comun/ItemListaComponent.vue';
import FabBotonComponent from '@/components/comun/FabBotonComponent.vue';

export default {
  components: { BuscadorComponent, ItemListaComponent, FabBotonComponent },
  props: {
    items: {
      required: true
    },
    cargando: {
      required: true
    },
    permisoCreacion: {
      type: Boolean,
      default: true
    },
    mostrarImagen: {
      type: Boolean,
      default: false
    },
    imagenPredeterminada: {
      type: String,
      default: ''
    }
  },
  computed: {
    acciones() {
      return this.permisoCreacion ? [{ icon: 'mdi-pencil', color: 'default', evento: 'editar' },
      { icon: 'mdi-trash-can', color: 'error', evento: 'eliminar' }] : [];
    }
  },
  data() {
    return {
      itemsFiltrados: [],
      filtrosCompletos: []
    };
  },
  watch: {
    items: {
      immediate: true,
      handler(newItems) {
        this.itemsFiltrados = newItems;
        if (newItems.length > 0) {
          this.filtrosCompletos = Object.keys(newItems[0]);
        }
      }
    }
  },
  methods: {
    updateItemsFiltrados(itemsFiltrados) {
      this.itemsFiltrados = itemsFiltrados;
    },
    abrirDialogoDetalle(item) {
      this.$emit('detalle', item);
    },
    emitEditar(item) {
      this.$emit('editar', item);
    },
    emitEliminar(item) {
      this.$emit('eliminar', item);
    },
    emitCrear() {
      this.$emit('crear');
    }
  }
};
</script>

<style scoped>
.fab-button {
  position: fixed;
  right: 16px;
  bottom: 16px;
}

.componente {
  margin-bottom: 20px !important;
  width: 100%;
}

.circulo-carga {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
</style>
