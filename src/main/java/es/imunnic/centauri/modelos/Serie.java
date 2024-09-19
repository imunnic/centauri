package es.imunnic.centauri.modelos;


import es.imunnic.centauri.entidades.Ejercicio;

public class Serie {

  private Ejercicio ejercicio;
  private int cantidad;
  private TipoDuracion tipo;
  private int carga;
  private boolean ajustable;

  public Serie(){
  }

  public Serie(Ejercicio ejercicio, int cantidad, TipoDuracion tipo, int carga, boolean ajustable) {
    setEjercicio(ejercicio);
    setCantidad(cantidad);
    setTipo(tipo);
    setCarga(carga);
    setAjustable(ajustable);
  }
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

  public TipoDuracion getTipo() {
    return tipo;
  }

  public void setTipo(TipoDuracion tipo) {
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


}
