package com.github.ecommerce.util;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${secret.key}")
	private String secretKey;
	
    public void validateToken(final String token) {
    	 Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }
    
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
