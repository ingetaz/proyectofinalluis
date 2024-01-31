package com.tlaxcala.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component // es un estereotipo genérico que no calza con ninguno de los vistos: service, controller, repository, etc...
public class JwtTokenUtil implements Serializable { // código java a jwt string especial

    //milisegundos
    public final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000; //5 horas de vigencia

    @Value("${jwt.secret}")  //EL Expression Language -> clave para firmar el token
    private String secret;

    public String generateToken(UserDetails userDetails){
        // defino que es lo que quiero proporcionar
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","))); // el get authority te devulve uno o mas roles como una cadena de texto concatenada por ,
        claims.put("test", "txalcala-test-value"); // credencial de prueba

        return doGenerateToken(claims, userDetails.getUsername()); // generamos el detalle con la info y el nombre del usuario (quien lo creo)
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder() // para construirlo
                .setClaims(claims) // crendeciales
                .setSubject(subject) // el usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // fecha de expiración
                .signWith(getSigningKey()) // la llave
                .compact(); // devuelve la cadena del token
    }

    private Key getSigningKey(){ // crea la llave de acuerdo a nuestra llave secreta
        return new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS512.getJcaName()); // el secret debe codificarse en base 64
    }

    //utils
    public Claims getAllClaimsFromToken(String token){ // con este metodo estamos recuperando el contenido del token
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){ // devuelve cualquier tipo de dato
        // esa function es una interfaz funcional que recibe lo que sea y devuelve lo que sea, recibe una solicitud para devolver un token
        final Claims claims = getAllClaimsFromToken(token); // esto me devuelve el body el payload
        return claimsResolver.apply(claims); // retorno el contenido resuleto de claims
    }

    public String getUsernameFromToken(String token){ // obtiene el usuario de las claims de acuerdo al token
        return getClaimFromToken(token, Claims::getSubject); // e-> e.getSubject()),
    }

    public Date getExpirationDateFromToken(String token){ // obtengo la fecha de expiración del token
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){ // para verificar si el token expiró
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date()); // la fecha de expiración está antes que la fecha actual
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
        // pregunto si el token pertenece a la sesión del usuario en spring y si no ha expirado
    }

}

