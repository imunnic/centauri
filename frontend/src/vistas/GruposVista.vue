<template>
    <div class="contenedor-flex">
        <ListaCrudComponent
        :accionesPersonalizadas="acciones"
        :items="solicitudesAcceso"
        :cargando="cargando"
        :permiso-creacion="true"
        @aceptar="validarSolicitud"
        @rechazar="negarSolicitud">
        </ListaCrudComponent>
    </div>
</template>
<script>
import ListaCrudComponent from '@/components/comun/ListaCrudComponent.vue';
import { useGruposStore } from '@/store/gruposStore.js';
import { useUsuariosStore } from '@/store/usuariosStore.js';
import { mapState, mapActions } from 'pinia';
export default {
    components:{ListaCrudComponent},
    data() {
        return {
            acciones:[
                {
                    icon:'mdi-thumb-up',
                    color:'var(--claro)',
                    evento:'aceptar'
                },
                {
                    icon:'mdi-thumb-down',
                    color:'var(--rechazo)',
                    evento:'rechazar'
                }
            ],
            cargando:false
        }
    },
    computed:{
        ...mapState(useGruposStore,['gruposEncargado','gruposRegistrados', 'solicitudesAcceso']),
        ...mapState(useUsuariosStore,['href'])
    },
    methods:{
        ...mapActions(useGruposStore,['getGruposEncargado', 'getSolicitudes', 'aceptarSolicitud', 'rechazarSolicitud']),
        async validarSolicitud(solicitud){
            await this.aceptarSolicitud(solicitud._links.self.href);
            this.cargando=false;
            await this.getSolicitudes();
            this.cargando = true;
        },
        async negarSolicitud(solicitud){
            await this.rechazarSolicitud(solicitud._links.self.href);
            this.cargando=true;
            await this.getSolicitudes();
            this.cargando = false;
        }
    },
    async mounted(){
        this.cargando = true;
        await this.getGruposEncargado(this.href);
        await this. getSolicitudes();
        this.cargando = false;
    }
}
</script>
<style scoped></style>