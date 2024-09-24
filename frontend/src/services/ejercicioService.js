import ejercicios from "../assets/ejercicios.json"

let config = {
  headers: {
    Authorization: "Bearer ",
  },
};

export default class EjerciciosService {
  constructor() {
  }

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  getAll() {
    return ejercicios;
  }

}
