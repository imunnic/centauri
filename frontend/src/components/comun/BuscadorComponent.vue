<!--
  Este componente es un campo de búsqueda que permite a los usuarios filtrar una lista de 
  elementos basada en una consulta de texto. Está apoyado en la librería de 
  Vuetify(https://vuetifyjs.com/). 

  Props:
  - items: Array de elementos a filtrar. Este array debe contener los datos que se quieren 
    buscar.
  - filtros: Array de claves que se usarán para buscar dentro de cada elemento del array `items`. 
    Cada clave corresponde a una propiedad del objeto `items` que será considerada en el filtro.

-->

<template>
  <div>
    <v-text-field
      class="placeholder"
      v-model="busqueda"
      placeholder="Buscar"
      label="Buscar"
      @input="listaFiltrada"
      append-inner-icon="mdi-magnify"
    />
  </div>
</template>

<script>
export default {
  props: {
    items: {
      type: Array,
      required: true,
    },
    filtros: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      busqueda: "",
    };
  },
  methods: {
    listaFiltrada() {
      const query = this.busqueda.toLowerCase();
      const cadenaPlana = (str) => {
        return str
          .normalize("NFD")
          .replace(/[\u0300-\u036f]/g, "")
          .toLowerCase();
      };
      const consultaPlana = cadenaPlana(query);
      let filtrado = this.filtros.filter((key) => key !== "id");
      const itemsfiltrados = this.items.filter((item) => {
        return filtrado.some((key) => {
          if (Array.isArray(item[key])) {
            return item[key].some((subItem) => {
              if (typeof subItem === "object" && subItem.nombre) {
                return cadenaPlana(subItem.nombre.toString()).includes(
                  consultaPlana
                );
              }
              return cadenaPlana(subItem.toString()).includes(
                consultaPlana
              );
            });
          } else if (item[key] !== null && item[key] !== undefined) {
            if (typeof item[key] === "object" && item[key].nombre) {
              return cadenaPlana(item[key].nombre.toString()).includes(
                consultaPlana
              );
            }
            return cadenaPlana(item[key].toString()).includes(
              consultaPlana
            );
          }
          return false;
        });
      });

      this.$emit("filtrada", itemsfiltrados);
    },
  },
};
</script>
