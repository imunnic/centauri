package es.mde.kiron.rest;

import es.mde.kiron.entidades.Ficha;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Configuration
public class ConfiguracionRest {
  /**
   * Enlaza automaticamente los links de los controladores registrados siguiendo las <a href=
   * "https://www.hijosdelspectrum.com/2020/05/codigo-util-clase-configuracionrest.html">instrucciones
   * </a>
   *
   * @param config {@link RepositoryRestConfiguration} para recuperar al {@code basePath}
   * @return el bean del tipo {@code RepresentationModelProcessor<RepositorySearchesResource>}
   */
  @Bean
  RepresentationModelProcessor<RepositorySearchesResource> addSearchLinks(
      RepositoryRestConfiguration config) {
    Map<Class<?>, Class<?>> controllersRegistrados = new HashMap<>();
    controllersRegistrados.put(Ficha.class, FichaController.class);

    return new RepresentationModelProcessor<>() {

      @Override
      public RepositorySearchesResource process(RepositorySearchesResource searchResource) {
        if (controllersRegistrados.containsKey(searchResource.getDomainType())) {
          Class<?> controller = controllersRegistrados.get(searchResource.getDomainType());
          Method[] metodos = controller.getDeclaredMethods();
          URI uriController = linkTo(controller).toUri();
          String controllerPath = config.getBasePath() + uriController.getPath();
          for (Method m : metodos) {
            if (!m.isAnnotationPresent(ResponseBody.class) || !m.isAnnotationPresent(
                GetMapping.class)) {
              continue;
            }
            try {
              String pathMetodo = String.join("", m.getAnnotation(GetMapping.class).value());
              String pathRecurso = new URI(uriController.getScheme(), uriController.getUserInfo(),
                  uriController.getHost(), uriController.getPort(), controllerPath + pathMetodo,
                  null, null).toString();
              String requestParams = Stream.of(m.getParameters())
                  .filter(p -> p.isAnnotationPresent(RequestParam.class)).map(Parameter::getName)
                  .collect(Collectors.joining(","));
              searchResource.add(
                  Link.of(URLDecoder.decode(pathRecurso, StandardCharsets.UTF_8) + "{?" + requestParams + "}",
                      m.getName()));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }

        return searchResource;
      }

    };
  }
}
