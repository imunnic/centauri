package es.imunnic.centauri.modelos;


import java.util.ArrayList;
import java.util.List;


public class Ronda {

  private List<Serie> series = new ArrayList<>();
  private TipoDuracion tipo;
  private int cantidad;

  public Ronda() {
    setSeries(new ArrayList<>());
  }

  public List<Serie> getSeries() {
    return series;
  }

  public void setSeries(List<Serie> series) {
    this.series = series;
  }

  public TipoDuracion getTipo() {
    return tipo;
  }

  public void setTipo(TipoDuracion tipo) {
    this.tipo = tipo;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }


  public void agregarSerie(Serie serie){
    getSeries().add(serie);
  }
}
