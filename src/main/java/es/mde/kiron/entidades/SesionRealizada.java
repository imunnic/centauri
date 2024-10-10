package es.mde.kiron.entidades;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class SesionRealizada {
  private String id;
  @DBRef
  private Usuario usuario;
  @DBRef
  private Sesion sesion;
  private int rpe;
  private int tiempo;
  private String comentarios;

  public SesionRealizada() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Sesion getSesion() {
    return sesion;
  }

  public void setSesion(Sesion sesion) {
    this.sesion = sesion;
  }

  public int getRpe() {
    return rpe;
  }

  public void setRpe(int rpe) {
    this.rpe = rpe;
  }

  public int getTiempo() {
    return tiempo;
  }

  public void setTiempo(int tiempo) {
    this.tiempo = tiempo;
  }

  public String getComentarios() {
    return comentarios;
  }

  public void setComentarios(String comentarios) {
    this.comentarios = comentarios;
  }

  public String getNombreSesion(){
    return getSesion().getNombre();
  }

  public LocalDate getFechaSesion(){
    return getSesion().getFecha();
  }

  public String getSesionId(){
    return getSesion().getId();
  }
}
