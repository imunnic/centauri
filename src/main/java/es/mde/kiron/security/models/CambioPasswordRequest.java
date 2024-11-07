package es.mde.kiron.security.models;

public class CambioPasswordRequest {
  private String username;
  private String passwordAntigua;
  private String passwordNueva;

  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswordAntigua() {
    return passwordAntigua;
  }
  public void setPasswordAntigua(String passwordAntigua) {
    this.passwordAntigua = passwordAntigua;
  }

  public String getPasswordNueva() {
    return passwordNueva;
  }

  public void setPasswordNueva(String passwordNueva) {
    this.passwordNueva = passwordNueva;
  }
}
