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
    config.headers.Authorization = config.headers.Authorization + token;
  }

  getAll() {
    return ejercicios;
  }

}
