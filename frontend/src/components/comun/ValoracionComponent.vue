<template>
  <div class="text-center">
    <v-rating v-model="valor" @click="emitirValoracion">
      <template v-slot:item="props">
        <v-icon
          :color="props.index === valor - 1 ? colores[props.index] : 'grey-lighten-1'"
          size="large"
          @click="props.onClick"
        >
          {{ props.index === valor - 1 ? iconosRellenos[props.index] : iconosSinRellenar[props.index] }}
        </v-icon>
      </template>
    </v-rating>
  </div>
</template>

<script>
export default {
  data() {
    return{
      colores: ['green', 'lime', 'yellow', 'orange', 'red'],
      iconosRellenos: [
        'mdi-emoticon-cool-outline',
        'mdi-emoticon-happy-outline',
        'mdi-emoticon-neutral-outline',
        'mdi-emoticon-sad-outline',
        'mdi-emoticon-sick-outline'
      ],
      iconosSinRellenar: [
        'mdi-emoticon-cool-outline',
        'mdi-emoticon-happy-outline',
        'mdi-emoticon-neutral-outline',
        'mdi-emoticon-sad-outline',
        'mdi-emoticon-sick-outline'
      ],
      valor: 3,
    }
  },
  methods: {
    emitirValoracion() {
      this.$emit('valor-seleccionado', this.valor);
    }
  },
  mounted() {
    this.emitirValoracion();
  },
}
</script>
