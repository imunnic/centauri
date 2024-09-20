package es.mde.kiron;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    {
        "classpath:configuracion/gestionDB.properties",
        "classpath:configuracion/secret.properties",

    })
public class ConfiguracionJava {

  @Bean
  public ObjectMapper getObjectMapper(){
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //mapper.addMixIn(Usuario.class, MixIns.Usuarios.class);
    return mapper;
  }

}
