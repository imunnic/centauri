import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "planes";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};
export default class PlanesService {
  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  async crearPlan(plan){
    return await axios.post(url,plan, config)
  }
}
