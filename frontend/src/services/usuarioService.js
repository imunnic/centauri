import axios from "axios";
import configuracion from "@/configuracion.json";

let url = configuracion.urlBase + "autenticacion/";
let login = "login";
let registro = "registro";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  }
};

export default class UsuariosService {
  constructor() {}

  async login(usuario) {
    const credenciales = {
      username: usuario.username,
      password: usuario.password,
    };
    let response = await axios.post(url + login, credenciales);
    config.headers.Authorization = "Bearer " + response.data.token;
    return response.data;
  }

  async getUsername() {
    const credenciales = {
      username: user,
      password: user,
    };
    let response = await axios.post(url + login, credenciales);
    return response.data.usuario;
  }

  async getRol() {
    const credenciales = {
      username: user,
      password: user,
    };
    let response = await axios.post(url + login, credenciales);
    return response.data.usuario.rol;
  }

  async getUsuario(href){
    return await axios.get(href,config);
  }
}
