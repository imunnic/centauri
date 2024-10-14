import axios from "axios";
import configuracion from "@/configuracion.json";
let url = configuracion.urlBase + "grupos";
let urlSolicitud = configuracion.urlBase + "solicitud-acceso-grupos";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  },
};

export default class GrupoService {
  constructor() {}

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

  async getGruposPorMiembro(hrefUsuario){
    let urlGetGruposPorMiembro = url + "/search/findByMiembrosContaining?miembro=" + hrefUsuario;
    return await axios.get(urlGetGruposPorMiembro, config);
  }

  async getGruposPorEncargado(hrefUsuario){
    let urlGetGruposPorEncargado = url + "/search/findByEncargado?encargado=" + hrefUsuario;
    return await axios.get(urlGetGruposPorEncargado, config);
  }

  async getGruposSinMiembro(idUsuario){
    let urlSinMiembro = url + "/encontrarGruposSinUsuario?usuarioId=" + idUsuario;
    return await axios.get(urlSinMiembro, config);
  }
  
  async postGrupo(grupo) {
    return await axios.post(url, grupo, config);
  }

  async existsGrupoByNombre(nombre){
    let urlExists = url + "/search/existsGrupoByNombreIgnoreCase?nombre=" + nombre;
    return await axios.get(urlExists, config);
  }

  async postSolicitudGrupo(solicitud){
    return await axios.post(urlSolicitud, solicitud, config);
  }

  async aceptarSolicitud(solicitud){
    let urlValidar = urlSolicitud + "/validar";
    return await axios.post(urlValidar, solicitud, config);
  }

  async rechazarSolicitud(solicitud){
    let urlRechazar = urlSolicitud + "/rechazar";
    return await axios.post(urlRechazar, solicitud, config);
  }

  async getSolicitudesGrupo(hrefGrupo){
    let urlSolicitudGrupo = urlSolicitud + "/search/findByGrupo?grupo=" + hrefGrupo;
    return await axios.get(urlSolicitudGrupo, config);
  }



}