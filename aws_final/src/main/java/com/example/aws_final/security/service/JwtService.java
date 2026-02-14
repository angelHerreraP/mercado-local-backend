package com.example.aws_final.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


/// Aqui fabricamos Tokens y lso desmenuzamos, basicmaente, sabemos que pedo con el token
@Service
public class JwtService {

    @Value("${app.jwt.secret}") // Llave entre applicatioin.properties y yop
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    //Genera el token
    public String generateToken(String username, Map<String, Object> extraClaims){
        return Jwts
                .builder()
                .setClaims(extraClaims)// Data como el rol y demas
                .setSubject(username) // De quien es el token...
                .setIssuedAt(new Date(System.currentTimeMillis())) //cuando se creo
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Cuando se muere el token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Firma digital
                .compact();
    }

    ///  REVISA SI EL TOKEN ESTA VIVO O NAH
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    ///  VERIFICA QUE ELE TOKEN NO HAYA EXPIRADO
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

//Obtener Username del token
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    //METODOS NECESARIOS
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Que hay en mi token, onbtener las partes
    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
