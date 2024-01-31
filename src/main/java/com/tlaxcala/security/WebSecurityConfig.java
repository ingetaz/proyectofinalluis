package com.tlaxcala.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // habilita todas las configs.
@EnableMethodSecurity // Es una especificación para que la seguridad pueda utilizar el método @PreAuthorize
@RequiredArgsConstructor
public class WebSecurityConfig { // utiliza todas las configuraciones ya realizadas y las activa en la app

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // instancia necesaria para propagar la autenticación de procesos
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // devuelve una instancia de password encriptado
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder()); // contrastar la información del user en cuanto al pass pasado vs lo que hay en bdd
    }

    // filtro que se debe agregar para todas las versiones de spring
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable) // deshabilita la protección de ataques para forms incrustados en el backend
            .authorizeHttpRequests(req -> req
                .requestMatchers("/login").permitAll() // ruta permitida para todos los usuarios
                //.requestMatchers("/patients/**").permitAll() | .authenticated()
                //.anyRequest().authenticated() // cualquier otra ruta de las no definidas le agrega el comportamiento
                //.requestMatchers("/v1/authenticate", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers( "/swagger-ui/**","/v3/api-docs/**", "/swagger-ui.html").permitAll()
            )
            .httpBasic(Customizer.withDefaults()) // proteger el contexto http desde el servidor anti ataques http
            .formLogin(AbstractHttpConfigurer::disable) // deshabilito el form basic de spring security
            .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // le damos la facultad a la clase interceptora de excepciones
            .sessionManagement(Customizer.withDefaults()); // para que se disponga de cara al manejo de sesiones

            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // compruebo primero mediante este filtro que viene el token para verificarlo de acuerdo al usuario a autenticar

            return null; //httpSecurity.build(); // retorno el bloque del filtro creado
    }
    
}
