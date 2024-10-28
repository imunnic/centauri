package es.mde.kiron.entidades;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
public class Grupo {
  private String id;
  @Indexed(unique = true)
  private String nombre;
  @DBRef
  private Usuario encargado;
  private String color;
  @DBRef
  private Set<Usuario> miembros;

  public Grupo() {
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

  public Usuario getEncargado() {
    return encargado;
  }

  public void setEncargado(Usuario encargado) {
    this.encargado = encargado;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Set<Usuario> getMiembros() {
    return miembros;
  }

  public void setMiembros(Set<Usuario> miembros) {
    this.miembros = miembros;
  }
}
