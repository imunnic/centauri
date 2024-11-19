package es.mde.kiron.entidades;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import java.util.Set;

public class SesionDePlan {
  private String nombre;
  @DBRef
  private Set<Ficha> fichas;
  private int dia;

  public SesionDePlan() {
    setFichas(new HashSet<>());
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Set<Ficha> getFichas() {
    return fichas;
  }

  public void setFichas(Set<Ficha> fichas) {
    this.fichas = fichas;
  }

  public int getDia() {
    return dia;
  }

  public void setDia(int dia) {
    this.dia = dia;
  }
}
