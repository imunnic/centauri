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

  async getPlanes(){
    let href = url + "/respuesta"
    return await axios.get(href,config);
  }

  async getPlan(id){
    let href = url + "/" + id;
    return await axios.get(href, config);
  }

  async agregarSesiones(agregarSesionesRequest){
    let href = url + "/" + agregarSesionesRequest.planId + "/agregarSesiones";
    return await axios.post(href, agregarSesionesRequest, config);
  }

  async eliminarPlan(planId){
    let href = url + "/" + planId;
    return await axios.delete(href, config);
  }

  async modificarPlan(plan){
    let href = url + "/" + plan.id
    return await axios.put(href,plan, config);

  }
}
