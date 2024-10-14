<!--
  Este componente representa un elemento en una lista, diseñado para ser utilizado dentro de un componente padre. Está construido con Vuetify (https://vuetifyjs.com/), permitiendo mostrar información y realizar acciones específicas sobre cada ítem.

  Props:
  - items: Array de objetos a mostrar.
      - `nombre`: El nombre del ítem.
      - `descripcion`: Una breve descripción del ítem.
      - `url`: (opcional) URL de la imagen asociada al ítem.
  - acciones: Array de objetos que define las acciones disponibles para el ítem. Cada objeto debe contener:
      - `icon`: El nombre del icono (de Material Design Icons).
      - `color`: El color del botón de acción.
      - `evento`: El evento que se emitirá al hacer clic en el botón.
    Por defecto, se incluyen dos acciones: editar (ícono 'mdi-pencil') y eliminar (ícono 'mdi-trash-can').
  - descripcion: Booleano que indica si se debe mostrar o no la descripción del ítem. El valor predeterminado es `true`.
  - mostrarImagen: Booleano que indica si se debe mostrar la imagen asociada al ítem. El valor predeterminado es `false`.
  - imagenPredeterminada: String que especifica la URL de una imagen predeterminada que se mostrará si el ítem no tiene una imagen propia. El valor por defecto es '/no-imagen.png'.

  Slots:
  - info-extra: Un slot opcional que permite inyectar contenido extra debajo de la descripción del ítem.

  Eventos:
  - editar: Se emite cuando se hace clic en el botón de editar.
  - eliminar: Se emite cuando se hace clic en el botón de eliminar.
  (Estos son los eventos predeterminados cuando se de permisos de creación, pero pueden variar si se modifican las acciones).
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
      <BuscadorComponent
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
        <template v-slot:info-extra="{ item }">
          <slot name="info-extra" :item="item"></slot>
        </template>
      </ItemListaComponent>

      <div class="contenedor-flex" v-if="itemsFiltrados.length == 0">
        No hay datos disponibles. Compruebe la conexión de red.
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
    accionesPersonalizadas: {
      type: Object,
      default: [
        { icon: "mdi-pencil", color: "default", evento: "editar" },
        { icon: "mdi-trash-can", color: "error", evento: "eliminar" },
      ],
    },
  },
  computed: {
    acciones() {
      return this.permisoCreacion ? this.accionesPersonalizadas : [];
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
