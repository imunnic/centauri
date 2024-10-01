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
      return axios.post(url, sesion, config);
    }

    async getSesionesPorGrupo(grupo){
      let href = url + "/search/findByGrupoIgnoreCase?grupo=" + grupo;
      return axios.get(href, config);
    }

    async getFichasDeSesion(href){
      return axios.get(href,config);
    }

    async eliminarSesion(href){
      return axios.delete(href, config)
    }
}