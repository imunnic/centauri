package es.mde.kiron.security.configuration;

import es.mde.kiron.security.jwt.JwtFiltroAutenticacion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author JOSE LUIS PUENTES ALAMOS
 */
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
  private final JwtFiltroAutenticacion FILTER;
  private final AuthenticationProvider PROVIDER;

  public ConfiguracionSeguridad(JwtFiltroAutenticacion FILTER, AuthenticationProvider PROVIDER) {
    this.FILTER = FILTER;
    this.PROVIDER = PROVIDER;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authRequest -> authRequest
            // Permitir el acceso público a los endpoints de autenticación y al de /aprobado
            .requestMatchers("/api/autenticacion/**", "/api/fichas/aprobado").permitAll()
            // Requerir autenticación para cualquier otro endpoint
            .anyRequest().authenticated())
        // Política de sesiones sin estado (stateless) para JWT o tokens Bearer
        .sessionManagement(sessionManager ->
            sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Proveedor de autenticación (usando tu autenticador personalizado)
        .authenticationProvider(this.PROVIDER)
        // Filtro personalizado (antes del UsernamePasswordAuthenticationFilter)
        .addFilterBefore(this.FILTER, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}

