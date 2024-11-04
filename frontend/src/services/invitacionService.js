import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "invitaciones";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};
export default class InvitacionesService {
  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  async crearInvitacion(){
    let post = {};
    return await axios.post(url,post, config)
  }
}
