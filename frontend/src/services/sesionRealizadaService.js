import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "sesiones-realizadas";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};

export default class SesionesRealizadasService {
  constructor() {}

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  async crearSesionRealizada(sesionRealizada) {
    return await axios.post(url, sesionRealizada, config);
  }

  async getSesionesRealizadasPorUsuario(hrefUsuario) {
    let href = url + "/search/findByUsuario?usuario=" + hrefUsuario;
    return await axios.get(href, config);
  }

  async getSesionesRealizadasPorSesion(hrefSesion) {
    let href = url + "/search/findByUsuario?usuario=" + hrefSesion;
    return await axios.get(href, config);
  }

  async getSesionesRealizadasPorUsuarioYSesion(hrefUsuario, hrefSesion) {
    let href =
      url +
      "/search/findByUsuarioAndSesion?usuario=" +
      hrefUsuario +
      "&sesion=" +
      hrefSesion;
    return await axios.get(href, config);
  }

  async getSesionesRealizadasPorUsuarioYGrupo(idUsuario, grupoNombre) {
    let href =
      url +
      "/buscar-sesion-usuario-grupo?usuarioId=" +
      idUsuario +
      "&grupo=" +
      grupoNombre;
    return await axios.get(href, config);
  }
}
