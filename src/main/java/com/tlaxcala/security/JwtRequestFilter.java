package com.tlaxcala.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter { // para interceptar las peticiones http

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (header != null) { // verificación de que viene el header
            if (header.startsWith("Bearer ") || header.startsWith("bearer ")) {
                jwtToken = header.substring(7); // obtener el valor del token en el header
                //jwtToken = header.split("")[1];

                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken); // obtención del username del usuario de acuerdo al token que viene
                } catch (Exception ex) {
                    request.setAttribute("exception", ex.getMessage());
                }
            }
        }

        if (username != null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()); // el atriburo 2 viene null para no exponer los pass en los request
                userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userPasswordAuthToken); // establece el bloque de autenticación de quien se logueo de acuerdo al contexto de credenciales en memoria. Esto me permitirá tener acceso al contexto para otras operaciones por ej: vinculación con roles y menus
            }
        }
        filterChain.doFilter(request, response);
    }
}