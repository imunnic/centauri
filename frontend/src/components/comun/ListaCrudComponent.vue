<!--
  Este componente representa una lista de elementos, diseñado para ser utilizado dentro de un componente padre. Está construido con Vuetify (https://vuetifyjs.com/), lo que permite mostrar información de manera estructurada y realizar acciones específicas sobre cada ítem.

  Props:
  - items: Array de objetos que se mostrarán en la lista. Este prop es obligatorio.
  - descripcion: Booleano que indica si se debe mostrar la descripción de cada ítem. El valor predeterminado es `true`.
  - cargando: Booleano que indica si la lista está en proceso de carga. Este prop es obligatorio.
  - permisoEdicion: Booleano que define si el usuario tiene permiso para editar los ítems. El valor predeterminado es `true`.
  - permisoCreacion: Booleano que define si el usuario tiene permiso para crear nuevos ítems. El valor predeterminado es `true`.
  - mostrarImagen: Booleano que indica si se debe mostrar la imagen asociada a cada ítem. El valor predeterminado es `false`.
  - imagenPredeterminada: String que especifica la URL de una imagen predeterminada que se mostrará si un ítem no tiene imagen propia. El valor predeterminado es una cadena vacía.
  - busqueda: Booleano que indica si se debe mostrar un componente de búsqueda para filtrar los ítems. El valor predeterminado es `true`.
  - accionesPersonalizadas: Array de objetos que define las acciones disponibles para cada ítem. Cada objeto debe contener:
      - `icon`: El nombre del icono (de Material Design Icons).
      - `color`: El color del botón de acción.
      - `evento`: El evento que se emitirá al hacer clic en el botón.
    Por defecto, se incluyen dos acciones: editar (ícono 'mdi-pencil') y eliminar (ícono 'mdi-trash-can').
  - titulo: String que se muestra como título de la lista. El valor predeterminado es una cadena vacía.

  Slots:
  - info-extra: Un slot opcional que permite inyectar contenido adicional debajo de cada ítem de la lista.

  Eventos:
  - detalle: Se emite cuando se hace clic en un ítem para proporcionar la información del mismo.
  - crear: Se emite cuando se desea crear un nuevo ítem (por ejemplo, al hacer clic en un botón flotante).
-->

<template>
  <v-container>
    <div v-if="cargando" class="circulo-carga">
      <v-progress-circular
        :size="70"
        :width="7"
        indeterminate
        color="primary"
      ></v-progress-circular>
    </div>

    <template v-else>
      <h2>{{ titulo }}</h2>
      <BuscadorComponent
        v-if="busqueda"
        class="componente"
        :items="items"
        :filtros="filtrosCompletos"
        @filtrada="updateItemsFiltrados"
      />

      <ItemListaComponent
        v-bind="$attrs"
        class="componente"
        v-for="(item, index) in itemsFiltrados"
        :key="index"
        :item="item"
        :acciones="acciones"
        :mostrar-imagen="mostrarImagen"
        :imagenPredeterminada="imagenPredeterminada"
        :descripcion="descripcion"
        @click="abrirDialogoDetalle(item)"
        v-on="$attrs"
      >
        <template v-slot:titulo="{ item }">
          <slot name="titulo" :item="item"></slot>
        </template>
        <template v-slot:info-extra="{ item }">
          <slot name="info-extra" :item="item"></slot>
        </template>
      </ItemListaComponent>

      <div class="contenedor-flex" v-if="itemsFiltrados.length == 0">
        No hay datos disponibles.
      </div>

      <FabBotonComponent v-if="permisoCreacion" @click="emitCrear('crear')" />
    </template>
  </v-container>
</template>

<script>
import BuscadorComponent from "@/components/comun/BuscadorComponent.vue";
import ItemListaComponent from "@/components/comun/ItemListaComponent.vue";
import FabBotonComponent from "@/components/comun/FabBotonComponent.vue";

export default {
  components: { BuscadorComponent, ItemListaComponent, FabBotonComponent },
  props: {
    items: {
      required: true,
    },
    descripcion: {
      default: true,
    },
    cargando: {
      required: true,
    },
    permisoEdicion:{
      type: Boolean,
      default: true,
    },
    permisoCreacion: {
      type: Boolean,
      default: true,
    },
    mostrarImagen: {
      type: Boolean,
      default: false,
    },
    imagenPredeterminada: {
      type: String,
      default: "",
    },
    busqueda:{
      type: Boolean,
      default:true
    },
    accionesPersonalizadas: {
      type: Object,
      default: [
        { icon: "mdi-pencil", color: "var(--claro)", evento: "editar" },
        { icon: "mdi-trash-can", color: "error", evento: "eliminar" },
      ],
    },
    titulo:{
      type:String,
      default:""
    }
  },
  computed: {
    acciones() {
      return this.permisoEdicion ? this.accionesPersonalizadas : [];
    },
  },
  data() {
    return {
      itemsFiltrados: [],
      filtrosCompletos: [],
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
      },
    },
  },
  methods: {
    updateItemsFiltrados(itemsFiltrados) {
      this.itemsFiltrados = itemsFiltrados;
    },
    abrirDialogoDetalle(item) {
      this.$emit("detalle", item);
    },
    emitCrear() {
      this.$emit("crear");
    },
  },
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
