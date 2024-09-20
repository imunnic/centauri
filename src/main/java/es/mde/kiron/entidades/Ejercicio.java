package es.mde.kiron.entidades;

import es.mde.kiron.modelos.TipoCarga;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;


public class Ejercicio {

  private String nombre;
  private String descripcion;
  private String cualidad;
  private int numero_ejecutantes;
  private String tipo_movimiento;
  private String tipo_contraccion;
  private String velocidad;
  private String url;
  private TipoCarga tipoCarga;
  private Set<String> musculosPrincipales = new HashSet<>();
  private Set<String> musculos_secundarios = new HashSet<>();

  public Ejercicio() {
    this.musculosPrincipales = new HashSet<>();
    this.musculos_secundarios = new HashSet<>();
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

  public int getNumero_ejecutantes() {
    return numero_ejecutantes;
  }

  public void setNumero_ejecutantes(int numero_ejecutantes) {
    this.numero_ejecutantes = numero_ejecutantes;
  }

  public String getTipo_movimiento() {
    return tipo_movimiento;
  }

  public void setTipo_movimiento(String tipo_movimiento) {
    this.tipo_movimiento = tipo_movimiento;
  }

  public String getTipo_contraccion() {
    return tipo_contraccion;
  }

  public void setTipo_contraccion(String tipo_contraccion) {
    this.tipo_contraccion = tipo_contraccion;
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

  public Set<String> getMusculos_secundarios() {
    return musculos_secundarios;
  }

  public void setMusculos_secundarios(Set<String> musculos_secundarios) {
    this.musculos_secundarios = musculos_secundarios;
  }

  public TipoCarga getTipoCarga() {
    return tipoCarga;
  }

  public void setTipoCarga(TipoCarga tipoCarga) {
    this.tipoCarga = tipoCarga;
  }
}
