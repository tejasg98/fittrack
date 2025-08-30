package com.appventory.fittrack.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {

    private static final String SECRET = "606316d26b2fdbea7f4be084ff547198652ddeca2183917611f617983d7382f144324a3caff16e354f3e768df9afdc4fa28a1708815877b422a41208ed06b386";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long ACCESS_TOKEN_VALIDITY = (60 * (60 * 1000)); // 60 mins

    // Generate access token
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
        	e.printStackTrace();
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
