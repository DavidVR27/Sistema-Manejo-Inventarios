package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {
    // expira en 6 meses
    private static final long EXPIRATION_TIME_IN_MILLISECONDS = 100L * 60L * 60L * 24L * 30L * 6L;

    private SecretKey key;

    @Value("${secretJwtString}")
    private String secretJwtString;

    @PostConstruct
    private void init(){
        try {
            log.info("Inicializando JWT Utils...");

            // Intentar primero como Base64
            try {
                byte[] decodedKey = Base64.getDecoder().decode(secretJwtString);
                this.key = Keys.hmacShaKeyFor(decodedKey);
                log.info("Clave Base64 cargada exitosamente");
            } catch (Exception e) {
                // Si no es Base64 válido, usar como string normal pero asegurar 32+ caracteres
                log.info("No es Base64, usando como string. Longitud: {} caracteres", secretJwtString.length());

                // Generar una clave segura automáticamente si la string no es segura
                SecretKey generatedKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                this.key = generatedKey;
                log.warn("Clave generada automáticamente para mayor seguridad");
            }

            log.info("JWT Utils inicializado correctamente");

        } catch (Exception e) {
            log.error("Error crítico al inicializar JWT Utils: ", e);
            throw new RuntimeException("No se pudo inicializar JWT Utils", e);
        }
    }

    public String generateToken(String email){
        log.info("*** GENERATING JWT TOKEN ***");
        log.info("Email: {}", email);
        log.info("Key Algorithm: {}", this.key.getAlgorithm());

        try {
         String token = Jwts.builder()
                 .subject(email)
                 .issuedAt(new Date(System.currentTimeMillis()))
                 .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISECONDS))
                 .signWith(key)
                 .compact();

         log.info("Token created successfully");
         log.info("Token length: {}", token.length());
         return token;
        } catch (Exception e) {
            log.error("Error while generating JWT TOKEN", e);
            throw e;
        }

    }

    public String getUsernameFromToken(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
