package es.imunnic.centauri.entidades;

import es.imunnic.centauri.seguridad.modelos.Rol;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario {

  @Id
  private String id;
  private String nombre;
  private String email;
  private String password;
  Rol rol;

  public Usuario() {
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
}
