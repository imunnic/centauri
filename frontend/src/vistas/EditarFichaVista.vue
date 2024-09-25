<template>
    <FichaFormComponent ref="formulario" :ficha="ficha"></FichaFormComponent>
</template>

<script>
import FichaFormComponent from '../components/FichaFormComponent.vue';
import { useFichasStore } from '@/store/fichasStore.js'
import { useUsuariosStore } from '@/store/usuariosStore';
import { useEjerciciosStore } from '@/store/ejerciciosStore.js'
import { mapState, mapActions } from 'pinia'
export default {
    components: {
        FichaFormComponent
    },
    computed:{
        ...mapState(useUsuariosStore,['token'])
    },
    data() {
        return {
            ficha:null
        }
    },
    methods: {
        ...mapActions(useEjerciciosStore, ['cargarEjercicios', 'arrancarServicioEjercicios']),
        ...mapActions(useFichasStore, ['cargarFichaDetalle'])
    },
    async created() {
        this.arrancarServicioEjercicios(this.token);
        await this.cargarEjercicios();

    },
    async mounted(){
        let apoyo = await this.cargarFichaDetalle(this.$route.params.id)
        this.ficha = apoyo.data;
        }
}
</script>

<style></style>