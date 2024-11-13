package es.mde.kiron.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class PlanDeEntrenamiento {
  @Id
  private String id;
  private String nombre;
  private String objetivo;
  private String descripcion;
  private List<SesionDePlan> sesiones;
  @DBRef
  private Usuario autor;

  public PlanDeEntrenamiento() {
    setSesiones(new ArrayList<>());
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getObjetivo() {
    return objetivo;
  }

  public void setObjetivo(String objetivo) {
    this.objetivo = objetivo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public List<SesionDePlan> getSesiones() {
    return sesiones;
  }

  public void setSesiones(List<SesionDePlan> sesiones) {
    this.sesiones = sesiones;
  }

  public Usuario getAutor() {
    return autor;
  }

  public void setAutor(Usuario autor) {
    this.autor = autor;
  }
}
