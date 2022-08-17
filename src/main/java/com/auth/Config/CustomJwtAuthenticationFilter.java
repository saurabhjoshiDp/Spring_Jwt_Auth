package com.auth.Config;


import com.auth.Model.UserDetailModel;
import com.auth.Utils.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try {
           String jwtToken = extractJwtToken(request);
           if (StringUtils.hasText(jwtToken) && jwtUtils.validateToken(jwtToken)) {
               UserDetails userDetails = new User(jwtUtils.getUserName(jwtToken), "", jwtUtils.getClaimFromToken(jwtToken));
               UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

           } else {
               System.out.println("Cannot set Security Context");
           }
       } catch (ExpiredJwtException e){
            request.setAttribute("exception",e);
       } catch(BadCredentialsException e){
            request.setAttribute("exception",e);
       }
        filterChain.doFilter(request,response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) &&  bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }
}
