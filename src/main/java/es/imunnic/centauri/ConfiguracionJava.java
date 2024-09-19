package es.imunnic.centauri;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    {
        "classpath:configuracion/bbdd.properties",
        "classpath:configuracion/secret.properties"

    })
public class ConfiguracionJava {
  @Bean
  public ObjectMapper getObjectMapper(){
    ObjectMapper mapper = new ObjectMapper();
    //mapper.addMixIn(Usuario.class, MixIns.Usuarios.class);
    return mapper;
  }

}
