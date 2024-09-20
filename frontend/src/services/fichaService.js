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
    return axios.get(url, config);
  }

  cambiarEstado(href, estado){
    let ruta;
    if(estado){
      ruta = href + "/estado?aprobado=true"
    } else{
      ruta = href + "/estado?aprobado=false"
    }
    return axios.patch(ruta, null, config)
  }

  getFicha(id){
    let ruta = url + "/" + id
    return axios.get(ruta, config)
  }

}
