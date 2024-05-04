package com.github.ecommerce.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	@Value("${secret.key}")
	private String secretKey;
	
    public String generateToken(String userName) {
        return createToken(new HashMap<>(), userName);
    }

    public String createToken(Map<String, Object> extraClaims, String userName ) {
        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (5 * 60 * 1000)))
                .and()
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
