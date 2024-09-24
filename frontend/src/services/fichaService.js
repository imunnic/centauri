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
    config.headers.Authorization = "Bearer " + token;
  }

  getAll() {
    return axios.get(url, config);
  }

  getAprobadas() {
    return axios.get(url + "/aprobado", config);
  }

  getPendientes(){
    return axios.get(url + "/pendiente", config);
  }

  cambiarEstado(href, estado){
    let ruta;
    if(estado){
      ruta = href + "/estado?aprobado=true";
    } else{
      ruta = href + "/estado?aprobado=false";
    }
    return axios.patch(ruta, null, config);
  }

  getFicha(id){
    let ruta = url + "/" + id;
    return axios.get(ruta, config);
  }

  getPropias(id){
    let ruta = url + "/search/findByAutor?id=" + id;
    return axios.get(ruta, config);
  }

  postFicha(ficha){
    return axios.post(url, ficha, config);
  }

  editarFicha(ficha, id){
    let ruta = url + "/" + id
    return axios.patch(ruta, ficha, config);
  }

}
