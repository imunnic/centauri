package es.mde.kiron.security.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class InvitacionRegistro {
  @Id
  private String id;

  @CreatedDate
  @Indexed(expireAfterSeconds = 86400)
  private Date createdAt;

  public InvitacionRegistro() {
    this.createdAt = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
