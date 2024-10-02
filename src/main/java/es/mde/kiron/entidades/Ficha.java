package es.mde.kiron.entidades;

import es.mde.kiron.modelos.Estado;
import es.mde.kiron.modelos.ParteSesion;
import es.mde.kiron.modelos.Ronda;
import es.mde.kiron.modelos.TipoFicha;
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
  private ParteSesion parteSesion;
  private TipoFicha tipoFicha;
  private String descripcion;
  @DBRef
  private Usuario autor;
  private List<Ronda> rutina = new ArrayList<>();
  private int rpeEstimado;
  private int tiempoEstimado;

  public Ficha() {
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

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Usuario getAutor() {
    return autor;
  }

  public void setAutor(Usuario autor) {
    this.autor = autor;
  }

  public int getRpeEstimado() {
    return rpeEstimado;
  }

  public void setRpeEstimado(int rpe) {
    this.rpeEstimado = rpe;
  }

  public int getTiempoEstimado() {
    return tiempoEstimado;
  }

  public void setTiempoEstimado(int tiempoEstimado) {
    this.tiempoEstimado = tiempoEstimado;
  }

  public ParteSesion getParteSesion() {
    return parteSesion;
  }

  public void setParteSesion(ParteSesion parteSesion) {
    this.parteSesion = parteSesion;
  }

  public TipoFicha getTipoFicha() {
    return tipoFicha;
  }

  public void setTipoFicha(TipoFicha tipoFicha) {
    this.tipoFicha = tipoFicha;
  }

}
