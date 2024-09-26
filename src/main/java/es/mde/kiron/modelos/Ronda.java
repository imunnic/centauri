package es.mde.kiron.modelos;


import java.util.ArrayList;
import java.util.List;

public class Ronda {

  private List<Serie> series = new ArrayList<>();
  private String tipo;
  private int cantidad;

  public List<Serie> getSeries() {
    return series;
  }

  public void setSeries(List<Serie> series) {
    this.series = series;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public Ronda() {
    setSeries(new ArrayList<>());
  }

  public void agregarSerie(Serie serie){
    getSeries().add(serie);
  }
}
