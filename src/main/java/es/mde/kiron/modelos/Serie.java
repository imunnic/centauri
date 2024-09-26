package es.mde.kiron.modelos;

import es.mde.kiron.entidades.Ejercicio;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Serie {

  private Ejercicio ejercicio;
  private int cantidad;
  private String tipo;
  private int carga;
  private boolean ajustable;

  public Ejercicio getEjercicio() {
    return ejercicio;
  }

  public void setEjercicio(Ejercicio ejercicio) {
    this.ejercicio = ejercicio;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public int getCarga() {
    return carga;
  }

  public void setCarga(int carga) {
    this.carga = carga;
  }

  public boolean isAjustable() {
    return ajustable;
  }

  public void setAjustable(boolean ajustable) {
    this.ajustable = ajustable;
  }

  public Serie(){
  }

  public Serie(Ejercicio ejercicio, int cantidad, String tipo, int carga, boolean ajustable) {
    setEjercicio(ejercicio);
    setCantidad(cantidad);
    setTipo(tipo);
    setCarga(carga);
    setAjustable(ajustable);
  }

}
