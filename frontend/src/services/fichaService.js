import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "fichas";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};

export default class FichaService {
  constructor() {}

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  async getAll() {
    return await axios.get(url, config);
  }

  async getAprobadas() {
    return await axios.get(url + "/aprobado", config);
  }

  async getPendientes() {
    return await axios.get(url + "/pendiente", config);
  }

  async cambiarEstado(href, estado) {
    let ruta;
    if (estado) {
      ruta = href + "/estado?aprobado=true";
    } else {
      ruta = href + "/estado?aprobado=false";
    }
    return await axios.patch(ruta, null, config);
  }

  async getFicha(id) {
    let ruta = url + "/" + id;
    return await axios.get(ruta, config);
  }

  async getPropias(id) {
    let ruta = url + "/search/findByAutor?id=" + id;
    return await axios.get(ruta, config);
  }

  async postFicha(ficha) {
    return await axios.post(url, ficha, config);
  }

  async editarFicha(ficha, id) {
    let ruta = url + "/" + id;
    return await axios.patch(ruta, ficha, config);
  }

  async deleteFicha(href) {
    return await axios.delete(href, config);
  }
}
