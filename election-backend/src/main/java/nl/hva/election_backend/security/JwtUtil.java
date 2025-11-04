package nl.hva.election_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.hva.election_backend.model.User;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "ThisIsAVeryStrongSecretKey123456789!!!";
    private static final byte[] SECRET_BYTES =
            Base64.getEncoder().encode(SECRET.getBytes());
    private static final long EXPIRATION_TIME = 3600 * 1000;

    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("name", user.getName());
        claims.put("userId", user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(new SecretKeySpec(
                        SECRET_BYTES,
                        SignatureAlgorithm.HS256.getJcaName()
                ))
                .compact();
    }

    public String validateTokenAndGetEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_BYTES)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // email van gebruiker
    }
}
