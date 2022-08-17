package com.auth.Utils;

import com.auth.Model.UserDetailModel;
import com.auth.Service.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JWTUtils {
    String secret;
    int expiryTimeMs;

    @Value("${jwt.secret}")
    public void setJwtToken(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.TTLms}")
    public void setExpiryTimeMs(int expiryTimeMs) {
        this.expiryTimeMs = expiryTimeMs;
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        if(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            claims.put("isAdmin",true);
        else if(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")))
            claims.put("isUser",true);
        return generateJwtToken(userDetails.getUsername(),claims);
    }
    private String generateJwtToken(String subject, Map<String, Object>claims){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTimeMs))
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    public boolean validateToken(String Token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(Token);
            return true;
        }catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex){
            throw new BadCredentialsException("INVALID_CREDENTIALS",ex);
        }catch (ExpiredJwtException e){
            throw e;
        }
    }

    public String getUserName(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public List<SimpleGrantedAuthority> getClaimFromToken(String token){
        List<SimpleGrantedAuthority> authorities = null;
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        Boolean isAdmin = claims.get("isAdmin",Boolean.class);
        Boolean isUser = claims.get("isUser",Boolean.class);
        if (isAdmin != null && isAdmin) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else if (isUser != null && isUser) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }
}
