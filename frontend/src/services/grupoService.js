import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "grupos";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};

export default class GrupoService {
  constructor() {}

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }
  
  async postGrupo(grupo) {
    return await axios.post(url, grupo, config);
  }
}