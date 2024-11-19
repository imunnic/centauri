package es.mde.kiron.modelos;

import es.mde.kiron.entidades.SesionRealizada;

import java.time.LocalDate;

public class SesionRealizadaResponse {
  private String id;
  private String usuarioNombre;
  private String nombre;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  private String sesionId;
  private String grupo;
  private LocalDate fecha;
  private int rpe;
  private int duracion;
  private String comentarios;

  public SesionRealizadaResponse() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsuarioNombre() {
    return usuarioNombre;
  }

  public void setUsuarioNombre(String usuarioNombre) {
    this.usuarioNombre = usuarioNombre;
  }

  public String getSesionId() {
    return sesionId;
  }

  public void setSesionId(String sesionId) {
    this.sesionId = sesionId;
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

  public int getRpe() {
    return rpe;
  }

  public void setRpe(int rpe) {
    this.rpe = rpe;
  }

  public int getDuracion() {
    return duracion;
  }

  public void setDuracion(int duracion) {
    this.duracion = duracion;
  }

  public String getComentarios() {
    return comentarios;
  }

  public void setComentarios(String comentarios) {
    this.comentarios = comentarios;
  }


  private static SesionRealizadaResponse convertirASesionRealizadaResponse(
      SesionRealizada sesionRealizada) {
    SesionRealizadaResponse response = new SesionRealizadaResponse();
    response.setId(sesionRealizada.getId());
    response.setUsuarioNombre(sesionRealizada.getUsuario().getNombre());
    response.setSesionId(sesionRealizada.getSesionId());
    response.setGrupo(sesionRealizada.getSesion().getGrupo());
    response.setFecha(sesionRealizada.getFechaSesion());
    response.setRpe(sesionRealizada.getRpe());
    response.setDuracion(sesionRealizada.getTiempo());
    response.setComentarios(sesionRealizada.getComentarios());
    return response;
  }
}
