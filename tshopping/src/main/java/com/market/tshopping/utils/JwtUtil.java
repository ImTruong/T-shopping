package com.market.tshopping.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKeyString;

    public String generateToken(String data){
        SecretKey secretKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)) ;
        String jws = Jwts.builder().setSubject(data).signWith(secretKey).compact();
        return jws;
    }

    public boolean verifyToken(String token){
        SecretKey secretKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)) ;
        try{
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        }catch(Exception e){
            return false;
        }

    }

    public String getUserNameFromToken(String token){
        SecretKey secretKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)) ;
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


}
