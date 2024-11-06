import axios from "axios";
import configuracion from "@/configuracion.json";

let url = configuracion.urlBase + "autenticacion/";
let login = "login";
let registro = "registro";
let renovacion = "renovacion";
let config = {
  headers: {
    Authorization: configuracion.headersDefecto.Authorization,
  }
};

export default class UsuariosService {

  actualizarCabecera(token) {
    config.headers.Authorization = "Bearer " + token;
  }

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

  async existeUsuario(nombreUsuario){
    let href = configuracion.urlBase + "usuarios/search/existsByNombre?nombre=" + nombreUsuario;
    return await axios.get(href,config);
  }

  async renovarToken(){
    return await axios.post(url + renovacion, null, config);
  }

  async registroUsuario(usuario){
    let user = {
      username: usuario.nombre,
      password: usuario.password,
      email: usuario.correo
    }
    return await axios.post(url + registro + "/" + usuario.invitacion, user, config);
  }

  async cambiarNombreUsuario(usuario){
    let href = configuracion.urlBase + "autenticacion/cambiar-nombre-usuario?nombre=" + usuario;
    return await axios.post(href, config);
  }

  async cambiarPassword(passwords){
    let href = configuracion.urlBase + "autenticacion/cambiar-password";
    return await axios.post(href, passwords, config);
  }

  async recuperarPassword(correo){
    let usuario = {
      email:correo
    }
    let href = configuracion.urlBase + "autenticacion/reset-password";
    return await axios.post(href,usuario,config);
  }
}
