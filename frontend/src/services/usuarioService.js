import axios from "axios";
import configuracion from "@/configuracion.json";

let url = configuracion.urlBase + "autenticacion/";
let login = "login";
let registro = "registro";
let bearerToken;
let user = "dparlop";

export default class UsuariosService {
    constructor(){}

    async login(){
        const credenciales = {
            username: user,
            password: user
        }
        let response = await axios.post(url + login,credenciales);
        return response.data;
    }

    async getUsername(){
        const credenciales = {
            username: user,
            password: user
        };
        let response = await axios.post(url + login,credenciales);
        return response.data.usuario;
    }

    async getRol(){
        const credenciales = {
            username: user,
            password: user
        };
        let response = await axios.post(url + login,credenciales);
        return response.data.usuario.rol;
    }
}