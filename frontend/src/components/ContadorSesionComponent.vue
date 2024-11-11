<template>
  <div class="claro">
    <p class="texto">
      {{ sesion.nombre }}
    </p>
    <ContadorFichaComponent 
    :key="fichaActual" 
    :ficha="ficha" 
    :marcas="marcas" 
    @ficha-finalizada="siguienteFicha" 
    @mostrar-ejercicio="mostrarEjercicio"/>
  </div>
</template>

<script>
import ContadorFichaComponent from "@/components/ContadorFichaComponent.vue";

export default {
  props:{
    sesion:{
      type:Object,
      required:true
    },
    marcas:{
      type:Object,
      required:true
    }
  },
  components: { ContadorFichaComponent },
  data() {
    return {
      ficha:{},
      fichaActual: 0,
    };
  },
  methods: {
    siguienteFicha() {
      this.fichaActual++;
      this.ficha = this.sesion.fichas[this.fichaActual];
      if (this.fichaActual >= this.sesion.fichas.length){
        this.$emit('sesion-finalizada');
      }
    },
    mostrarEjercicio(ejercicio){
      this.$emit('mostrar-ejercicio', ejercicio);
    }
  },
  created() {
    this.ficha = this.sesion.fichas[this.fichaActual];
  },
};
</script>
<style scoped>
.texto{
  text-align: center;
  font-size: xx-large;
}
.claro{
  min-height: 86vh;
  align-items: start;
}
.claro:hover{
  background-color: initial !important;
}
</style>