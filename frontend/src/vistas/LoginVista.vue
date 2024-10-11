<template>
  <v-container class="contenedor-flex">
    <v-card elevation="3" class="carta">
      Registro
      <v-form class="formulario" @keyup.enter="intentarLogin()">
        <v-text-field
          class="placeholder"
          color="black"
          v-model="logeo.username"
          variant="outlined"
          :rules="[reglas.required]"
          label="Nombre de usuario"
        >
        </v-text-field>

        <v-text-field
          v-model="logeo.password"
          class="placeholder"
          variant="outlined"
          :rules="[reglas.required]"
          :append-inner-icon="mostrar ? 'mdi-eye' : 'mdi-eye-off'"
          label="Contraseña"
          :type="mostrar ? 'text' : 'password'"
          @click:append-inner="mostrar = !mostrar"
        >
          <template v-if="intentos > 0" #details>
            <v-spacer />
            <p class="intentoFallido">Contraseña o usuario incorrectos</p>
          </template>
        </v-text-field>

        <v-btn aria-label="login" class="claro" @click="intentarLogin()"> Entrar </v-btn>
      </v-form>
    </v-card>
  </v-container>
</template>

<script>
import { useUsuariosStore } from "@/store/usuariosStore.js";
import { useFichasStore } from "@/store/fichasStore.js";
import { useEjerciciosStore } from "@/store/ejerciciosStore.js";
import { useEquipamientosStore } from "@/store/equipamientosStore.js";
import { useSesionesStore } from "@/store/sesionesStore.js";
import { useSesionesRealizadasStore } from "@/store/sesionesRealizadasStore.js";
import { useGruposStore } from "@/store/gruposStore.js"

import { mapState, mapActions } from "pinia";
export default {
  computed: {
    ...mapState(useUsuariosStore, ["token", "isLogged", "login"]),
  },
  data() {
    return {
      logeo: {
        username: "",
        password: "",
      },
      reglas: {
        required: (value) => !!value || "Campo requerido",
      },
      mostrar: false,
      intentos: 0,
    };
  },
  methods: {
    ...mapActions(useUsuariosStore, ["peticionLogin"]),
    ...mapActions(useFichasStore, ["arrancarServicioFicha"]),
    ...mapActions(useEjerciciosStore, ["arrancarServicioEjercicios"]),
    ...mapActions(useEquipamientosStore, ["arrancarServicioEquipamientos"]),
    ...mapActions(useSesionesStore, ["arrancarServicioSesion"]),
    ...mapActions(useSesionesRealizadasStore, ["arrancarServicioSesionRealizada"]),
    ...mapActions(useGruposStore, ["arrancarServicioGrupos"]),

    async intentarLogin() {
      try {
        await this.peticionLogin(this.logeo);
        this.arrancarServicioFicha(this.token);
        this.arrancarServicioEjercicios(this.token);
        this.arrancarServicioEquipamientos(this.token);
        this.arrancarServicioSesion(this.token);
        this.arrancarServicioSesionRealizada(this.token);
        this.arrancarServicioGrupos(this.token);
        this.$router.push("/usuario")
      } catch (error) {
        console.error("Error en el login:", error);
      }
    },
  },
  async created() {},
};
</script>
<style scoped>
.intentoFallido {
  color: var(--rechazo);
}

.formulario {
  display: flex;
  flex-flow: column;
  align-items: center;
  min-width: 300px;
  width: 75%;
  padding: 10px;
}

.carta{
  padding: 10px;
}
.carta::before{
  width: 0px;
}

.v-text-field {
  width: 75%;
}

.contenedor {
  display: flex;
  justify-content: center;

}

@media (max-width: 800px) {
  .card {
    width: 55%;
  }
}

@media (max-width: 550px) {
  .card {
    width: 90%;
  }
}
</style>
