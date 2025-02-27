package com.ecom.apis.Jwt;

import com.ecom.apis.config.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Autowired
    JwtService jwtService;
    @Autowired
    UserDetailService userDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticator = request.getHeader("Authorization");
        String token =null;
        String username=null;
        if(authenticator!= null && authenticator.startsWith("Bearer")) {
            token = authenticator.substring(7);
            username = jwtService.extractUsername(token);
        }
        if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if (jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                if(username!=null) setUser(username);
            }
        }
        filterChain.doFilter(request,response);
    }
}
