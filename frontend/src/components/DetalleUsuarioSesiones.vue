<template>
  <v-dialog v-model="dialog" persistent max-width="600px"> 
    <v-card>
      <v-card-title class="d-flex justify-space-between">
        <span>{{ titulo }}</span>
        <v-btn aria-label="cerrar" icon @click="cerrar" flat>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <ListaCrudComponent
        :items="sesionesRealizadas"
        :permiso-creacion="false"
        :permiso-edicion="false"
        :cargando="cargando"
      >
      <template v-slot:info-extra="{ item }">
          <div>
            <p><b>{{ item.fecha[2] }}/{{ item.fecha[1] }}/{{ item.fecha[0] }}</b></p>
            <p><b>RPE:</b> {{ item.rpe }}</p>
            <p><b>Duración:</b> {{ item.duracion }}</p>
            <p><b>Comentarios:</b> {{ item.comentarios }}</p>
          </div>
        </template>
    </ListaCrudComponent>
    </v-card>
  </v-dialog>
</template>
<script>
import ListaCrudComponent from "@/components/comun/ListaCrudComponent.vue";
export default {
  components: {ListaCrudComponent},
  props: {
    sesionesRealizadas: {
      type: Array,
      required: true,
    },
    cargando:false
  },
  computed:{
    titulo(){
      if (this.sesionesRealizadas.length > 0){
        return 'Sesiones realizadas de ' +  this.sesionesRealizadas[0].usuarioNombre
      } else {
        return 'No hay sesiones realizadas de este usuario aún'
      }
    }
  },
  data() {
    return {
      dialog: true,
    };
  },
  methods: {
    cerrar() {
      this.dialog = false;
      this.$emit("cerrar");
    },
  },
};
</script>
<style scoped></style>
