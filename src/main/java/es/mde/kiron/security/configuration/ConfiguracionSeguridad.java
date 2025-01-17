package es.mde.kiron.security.configuration;

import es.mde.kiron.security.jwt.JwtFiltroAutenticacion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author JOSE LUIS PUENTES ALAMOS
 * Adaptada por IGNACIO OVIDIO MUÑOZ NICOLÁS
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
            .requestMatchers("/api/autenticacion/cambioPassword").authenticated()
            .requestMatchers("/api/autenticacion/login",
                "/api/autenticacion/registro/**",
                "/api/autenticacion/reset-password").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/invitaciones").authenticated()
            .requestMatchers(HttpMethod.GET,"/api/invitaciones/**").permitAll()
            .requestMatchers("/api/fichas/aprobado").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/fichas/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/ejercicios").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/planes").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/planes/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/ejercicios/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/equipamientos").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/usuarios/existe-email").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/usuarios/search/existsByNombre").permitAll()
            .anyRequest().authenticated())
        .sessionManagement(sessionManager ->
            sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(this.PROVIDER)
        .addFilterBefore(this.FILTER, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}

