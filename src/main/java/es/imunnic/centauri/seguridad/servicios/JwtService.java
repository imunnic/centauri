package es.imunnic.centauri.seguridad.servicios;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author JOSE LUIS PUENTES ALAMOS
 */
@Service
public class JwtService {
  private final static String SECRET_KEY = crearSecretKey();

  public String getToken(UserDetails usuario){
    return getToken(new HashMap<>(), usuario);
  }

  private String getToken(Map<String, Object> extraClaims, UserDetails usuario){
    return Jwts
        .builder()
        .claims(extraClaims)
        .subject(usuario.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000*60*24))
        .signWith(getKey())
        .compact();
  }

  private SecretKey getKey(){
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String getUsernameFromToken(String token){
    return getClaim(token, Claims::getSubject);
  }

  public boolean isTokenValido(String token, UserDetails userDetails){
    final String username = getUsernameFromToken(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpirado(token);
  }

  private Claims getAllClaims(String token){
    return Jwts
        .parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = getAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Date getFechaExpiracion(String token){
    return getClaim(token, Claims::getExpiration);
  }

  private boolean isTokenExpirado(String token){
    return getFechaExpiracion(token).before(new Date());
  }

  private static String crearSecretKey() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] secretKeyBytes = new byte[32];
    secureRandom.nextBytes(secretKeyBytes);
    return Base64.getEncoder().encodeToString(secretKeyBytes);
  }

}

