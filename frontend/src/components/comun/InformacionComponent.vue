<!--
  Este componente muestra información adicional a través de un ícono interactivo con tooltip, 
  facilitado por la librería Vuetify (https://vuetifyjs.com/). Está compuesto por una etiqueta 
  descriptiva y un ícono de información que, al ser activado, despliega un mensaje dentro del tooltip.

  Props:
  - texto: String que contiene el mensaje que se mostrará dentro del tooltip. Es un campo 
    obligatorio.
  - etiqueta: String opcional que proporciona una breve descripción o título antes del ícono 
    informativo.
-->

<template>
  <div class="marca-info">
    <div class="input-label">{{ etiqueta }}</div>
    <v-tooltip :text="texto" class="tooltip" max-width="200px">
      <template v-slot:activator="{ props }">
        <v-icon v-bind="props" icon="mdi-information-outline"></v-icon>
      </template>
    </v-tooltip>
  </div>
</template>
<script>
export default {
  props: {
    texto: {
      type: String,
      required: true,
    },
    etiqueta: {
      type:String,
      required:false
    }
  },
};
</script>
<style scoped>
.tooltip {
  max-width: 40vw;
}

.marca-info{
  display: flex;
  flex-flow: row;
  gap:5px;

}
</style>
