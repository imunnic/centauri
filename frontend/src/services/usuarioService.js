import axios from "axios";
let url = "http://localhost:8080/api/autenticacion/";
let login = "login";
let registro = "registro";
let bearerToken;
let user = "dparlop";

export default class UsuariosService {
    constructor(){}

    async getToken(){
        const credenciales = {
            username: user,
            password: user
        };
        let response = await axios.post(url + login,credenciales);
        return response.data.token;
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