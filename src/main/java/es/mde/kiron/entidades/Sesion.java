package es.mde.kiron.entidades;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Document
public class Sesion {
  private String id;
  private String nombre;
  private String unidad;
  private String grupo;
  private LocalDate fecha;
  @DBRef
  private Set<Ficha> fichas;

  public Sesion() {
    setFichas(new HashSet<Ficha>());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public String getGrupo() {
    return grupo;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public Set<Ficha> getFichas() {
    return fichas;
  }

  public void setFichas(Set<Ficha> fichas) {
    this.fichas = fichas;
  }
}
