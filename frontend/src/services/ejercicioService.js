import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "ejercicios";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};
export default class EjerciciosService {
  constructor() {}

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  async getAll() {
    let ejercicios = [];
    let paginaActual = 0;
    let totalPaginas = 1;
    let urlPagina = url + "?page=" + paginaActual;

    while (paginaActual < totalPaginas) {
      try {
        const response = await axios.get(urlPagina, config);
        ejercicios = ejercicios.concat(response.data._embedded.ejercicios);
        totalPaginas = response.data.page.totalPages;
        
        if (paginaActual <= totalPaginas) {
          paginaActual++;
          urlPagina = url + "?page=" + paginaActual;
        }
      } catch (error) {
        console.error("Error al obtener los datos:", error);
        break;
      }
    }

    return ejercicios;
  }

  crearEjercicio(ejercicio) {
    return axios.post(url, ejercicio, config);
  }
}
