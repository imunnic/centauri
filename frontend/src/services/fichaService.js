import axios from "axios";
let url = "http://localhost:8080/api/fichas";
let config = {
  headers: {
    Authorization: "Bearer ",
  },
};

export default class FichaService {
  constructor() {}

  actualizarCabecera(token) {
    config.headers.Authorization = config.headers.Authorization + token;
  }

  getAll() {
    console.log(url);
    console.log(config);
    return axios.get(url, config);
  }

  cambiarEstado(href, estado){
    let ruta = href + "/estado"
    return axios.patch(ruta, estado)
  }

}
