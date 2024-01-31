package com.tlaxcala.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // primer filtro de accesibilidad al servidor 
public class CORS implements Filter {

    // ciclo de vida del filtro
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // inicializar la política de funcionamiento de CORS

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", "*"); // especificamos hacia que dominios queremos exponer nuestros servicios
        response.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT");
        response.setHeader("Access-Control-Max-Age", "3600"); // tiempo de reconocimiento de los navegadores para peticiones
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN"); // atributos necesarios a manejar en los request con seguridad

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // para verificar el status de respuesta de una petición
        } else {
            chain.doFilter(req, res); // continua con las solicitudes normal con otros verbos si no es options
        }
    }

    @Override
    public void destroy() {
        // método de ciclo de vida para limpiezas del filtro cuando el servidor no es solicitado

    }

}
