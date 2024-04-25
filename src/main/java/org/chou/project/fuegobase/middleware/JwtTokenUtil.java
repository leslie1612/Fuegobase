package org.chou.project.fuegobase.middleware;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {


    private @Value("${jwt.signKey}") String jwtSignKey;

    private @Value("${jwt.expireTimeAsSec}") long jwtExpireTimeAsSec; // 30 day in sec

    public Boolean validate(String token) {
        final String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token));
    }

    public String generateToken(Map<String, Object> payload, String subject) {
        JwtBuilder jwtBuilder = Jwts.builder();
        if (payload != null) {
            Claims useClaims = Jwts.claims(payload);
            jwtBuilder.setClaims(useClaims);
        }
        return jwtBuilder
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(jwtExpireTimeAsSec)))
                .signWith(SignatureAlgorithm.HS256, generateKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

//    @SuppressWarnings("unchecked")
//    public List<SimpleGrantedAuthority> getUserAuthoritiesFromToken(String token) {
//        List<String> userRoles = parseToken(token).get(CLAIMS_KEY_USER_ROLES, List.class);
//        return userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }

    public Date getExpirationDateFromToken(String token) {
        return parseToken(token).getExpiration();
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(jwtSignKey.getBytes());
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
