package es.imunnic.centauri.seguridad.modelos;

import es.imunnic.centauri.entidades.Usuario;

/**
 * @author JOSE LUIS PUENTES ALAMOS
 */
public class AutenticacionResponse {
  private String token;
  private String username;
  private Usuario usuario;
  private Rol rol;

  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol){
    this.rol = rol;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public AutenticacionResponse() {
  }

  public AutenticacionResponse(String token, String username, Rol rol) {
    this.token = token;
    this.username = username;
    this.rol = rol;
  }

  public AutenticacionResponse(String token, String username, Rol rol, Usuario usuario) {
    this.token = token;
    this.username = username;
    this.rol = rol;
    this.usuario = usuario;
  }

}

