import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "equipamientos";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};

export default class EquipamientosService {

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  async getAll() {
    let equipamientos = [];
    let paginaActual = 0;
    let totalPaginas = 1;
    let urlPagina = url + "?page=" + paginaActual;

    while (paginaActual < totalPaginas) {
      try {
        const response = await axios.get(urlPagina, config);
        equipamientos = equipamientos.concat(
          response.data._embedded.equipamientos
        );
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

    return equipamientos;
  }

  async getEquipamiento(href) {
    return await axios.get(href, config);
  }

  async crearEquipamiento(equipamiento) {
    return await axios.post(url, equipamiento, config);
  }

  async modificarEquipamiento(equipamientoModificado) {
    return await axios.put(
      equipamientoModificado._links.self.href,
      equipamientoModificado,
      config
    );
  }

  async eliminarEquipamiento(href) {
    return await axios.delete(href, config);
  }
}
