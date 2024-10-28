package es.mde.kiron.rest;

public class AgregarMiembroRequest {
  private String solicitudId;
  private Boolean aceptar;

  public AgregarMiembroRequest() {
  }

  public String getSolicitudId() {
    return solicitudId;
  }

  public void setSolicitudId(String solicitudId) {
    this.solicitudId = solicitudId;
  }

  public Boolean getAceptar() {
    return aceptar;
  }

  public void setAceptar(Boolean aceptar) {
    this.aceptar = aceptar;
  }
}
