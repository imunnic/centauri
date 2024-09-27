package es.mde.kiron.entidades;

import es.mde.kiron.modelos.TipoCarga;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Document
public class Ejercicio {

  @Id
  private String id;
  private String nombre;
  private String descripcion;
  private String cualidad;
  private int numeroEjecutantes;
  private String tipoMovimiento;
  private String tipoContraccion;
  private String velocidad;
  @DBRef
  private Equipamiento equipamiento;
  private String url;
  private TipoCarga tipoCarga;
  private Set<String> musculosPrincipales = new HashSet<>();
  private Set<String> musculosSecundarios = new HashSet<>();

  public Ejercicio() {
    this.musculosPrincipales = new HashSet<>();
    this.musculosSecundarios = new HashSet<>();
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

  public String getCualidad() {
    return cualidad;
  }

  public void setCualidad(String cualidad) {
    this.cualidad = cualidad;
  }

  public int getNumeroEjecutantes() {
    return numeroEjecutantes;
  }

  public void setNumeroEjecutantes(int numeroEjecutantes) {
    this.numeroEjecutantes = numeroEjecutantes;
  }

  public String getTipoMovimiento() {
    return tipoMovimiento;
  }

  public void setTipoMovimiento(String tipoMovimiento) {
    this.tipoMovimiento = tipoMovimiento;
  }

  public String getTipoContraccion() {
    return tipoContraccion;
  }

  public void setTipoContraccion(String tipoContraccion) {
    this.tipoContraccion = tipoContraccion;
  }

  public String getVelocidad() {
    return velocidad;
  }

  public void setVelocidad(String velocidad) {
    this.velocidad = velocidad;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Set<String> getMusculosPrincipales() {
    return musculosPrincipales;
  }

  public void setMusculosPrincipales(Set<String> musculosPrincipales) {
    this.musculosPrincipales = musculosPrincipales;
  }

  public Set<String> getMusculosSecundarios() {
    return musculosSecundarios;
  }

  public void setMusculosSecundarios(Set<String> musculosSecundarios) {
    this.musculosSecundarios = musculosSecundarios;
  }

  public TipoCarga getTipoCarga() {
    return tipoCarga;
  }

  public void setTipoCarga(TipoCarga tipoCarga) {
    this.tipoCarga = tipoCarga;
  }

  public Equipamiento getEquipamiento() {
    return equipamiento;
  }

  public void setEquipamiento(Equipamiento equipamiento) {
    this.equipamiento = equipamiento;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
