package es.imunnic.centauri.entidades;

import es.imunnic.centauri.modelos.Estado;
import es.imunnic.centauri.modelos.Ronda;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Ficha {
  @Id
  private String id;
  private Estado estado;
  private String nombre;
  private String observaciones;
  @DBRef
  private Usuario autor;
  private String unidad;
  private String categoria;
  private List<Ronda> rutina = new ArrayList<>();
  private int rpe;
  private int tiempoEstimado;

  public Ficha() {
  }

  public Ficha(String nombre, String observaciones, Usuario autor, String unidad,
      String categoria, List<Ronda> rutina, int rpe, int tiempoEstimado) {

    this.estado = Estado.PENDIENTE;
    this.nombre = nombre;
    this.observaciones = observaciones;
    this.autor = autor;
    this.unidad = unidad;
    this.categoria = categoria;
    this.rutina = rutina;
    this.rpe = rpe;
    this.tiempoEstimado = tiempoEstimado;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Estado getEstado() {
    return this.estado;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }

  public List<Ronda> getRutina() {
    return rutina;
  }

  public void setRutina(List<Ronda> rutina) {
    this.rutina = rutina;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Usuario getAutor() {
    return autor;
  }

  public void setAutor(Usuario autor) {
    this.autor = autor;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public int getRpe() {
    return rpe;
  }

  public void setRpe(int rpe) {
    this.rpe = rpe;
  }

  public int getTiempoEstimado() {
    return tiempoEstimado;
  }

  public void setTiempoEstimado(int tiempoEstimado) {
    this.tiempoEstimado = tiempoEstimado;
  }
}
