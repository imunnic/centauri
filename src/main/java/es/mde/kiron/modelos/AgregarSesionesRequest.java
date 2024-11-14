package es.mde.kiron.modelos;

import java.time.LocalDate;

public class AgregarSesionesRequest {
  private LocalDate fecha;
  private String grupo;
  private String unidad;

  public AgregarSesionesRequest() {
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public String getGrupo() {
    return grupo;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }
}
