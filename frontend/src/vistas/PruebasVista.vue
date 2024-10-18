<template>
    {{ cantidad }}
    <InputTiempoComponent @nuevoValor="actualizadCantidad"></InputTiempoComponent>
</template>
<script>
import InputTiempoComponent from "@/components/comun/InputTiempoComponent.vue";
export default {
    components:{InputTiempoComponent},
    data() {
        return {
            cantidad:0
        }
    },
    methods: {
        actualizadCantidad(valor){
            console.log(valor)
            // this.cantidad = valor.data
        }
    }
    
}
</script>
<style scoped>
</style>