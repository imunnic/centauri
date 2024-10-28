package es.mde.kiron.modelos;

public class ResumenSesionesGrupo {
  private String nombre;
  private String sesionId;
  private int rpeMedio;
  private int tiempoMedio;

  public ResumenSesionesGrupo() {
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getSesionId() {
    return sesionId;
  }

  public void setSesionId(String sesionId) {
    this.sesionId = sesionId;
  }

  public int getRpeMedio() {
    return rpeMedio;
  }

  public void setRpeMedio(int rpeMedio) {
    this.rpeMedio = rpeMedio;
  }

  public int getTiempoMedio() {
    return tiempoMedio;
  }

  public void setTiempoMedio(int tiempoMedio) {
    this.tiempoMedio = tiempoMedio;
  }
}
