package es.mde.kiron.entidades;

import es.mde.kiron.security.models.Rol;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "usuarios")
public class Usuario {

  @Id
  private String id;
  @Indexed(unique = true)
  private String nombre;
  private Map<String, Integer> marcas;
  Rol rol;

  public Usuario(){
    this.marcas=new HashMap<>();
  }

  public Usuario(String nombre, Rol rol) {
    this.nombre = nombre;

    this.rol = rol;
  }

  public String getNombre() {
    return nombre;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

  public Map<String, Integer> getMarcas() {
    return marcas;
  }

  public void setMarcas(Map<String, Integer> marcas) {
    this.marcas = marcas;
  }
}
