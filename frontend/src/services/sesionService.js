import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "sesiones";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};

export default class sesionesService {
    constructor() {}
  
    actualizarCabecera(token) {
      config.headers.Authorization = "Bearer " + token;
    }

    async crearSesion(sesion){
      return await axios.post(url, sesion, config);
    }

    async getSesionesPorGrupo(grupo){
      let href = url + "/search/findByGrupoIgnoreCase?grupo=" + grupo;
      return await axios.get(href, config);
    }

    async getFichasDeSesion(href){
      return await axios.get(href,config);
    }

    async editarSesion(sesion){
      return await axios.patch(sesion.href, sesion, config);
    }

    async eliminarSesion(href){
      return await axios.delete(href, config);
    }

    async validarFichasSesion(sesion){
      let href = url + "/validar"
      return await axios.post(href, sesion, config);
    }

    async getFichasDeSesionConId(sesionId){
      let href = url + "/" + sesionId + "/fichas";
      return await axios.get(href, config);
    }
}