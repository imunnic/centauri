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

  async modificarEjercicio(ejercicioModificado) {
    return await axios.patch(
      ejercicioModificado._links.self.href,
      ejercicioModificado,
      config
    );
  }

  async eliminarEjercicio(href) {
    await axios.delete(href, config);
  }

  async crearEjercicio(ejercicio) {
    console.log(ejercicio);
    return await axios.post(url, ejercicio, config);
  }
  async getEquipamientoDeEjercicio(href) {
    return await axios.get(href, config);
  }
}
