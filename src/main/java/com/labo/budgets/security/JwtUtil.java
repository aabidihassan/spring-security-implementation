package com.labo.budgets.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.labo.budgets.models.Utilisateur;
import com.labo.budgets.services.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtUtil {

	public static final String SECRET = "mySecret123";
    public static final String AUTH_HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final long EXPIRE_ACCESS_TOKEN = 60*60*1000;
    public static final long EXPIRE_REFRESH_TOKEN = 192*60*60*1000;


    private static final Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);

    public static String createAccessToken(User user, String url){
        Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRE_ACCESS_TOKEN))
                .withIssuer(url)
                .withClaim("roles", user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static String createRefreshToken(User user, String url){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRE_REFRESH_TOKEN))
                .withIssuer(url)
                .withClaim("roles", user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
                .sign(JwtUtil.algorithm);
    }

    public static String createAccessTokenFromRefreshToken(String jwt, String url, UtilisateurService utilisateurService){
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        String username = decodedJWT.getSubject();
        Utilisateur user = utilisateurService.loadUserByUsername(username);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRE_ACCESS_TOKEN))
                .withIssuer(url)
                .withClaim("roles", user.getRoles().stream().map(ga->ga.getLibelle()).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static void refreshToken(HttpServletRequest request, HttpServletResponse response, UtilisateurService utilisateurService) throws Exception{
        String authToken = request.getHeader(JwtUtil.AUTH_HEADER);
        if(authToken!=null && authToken.startsWith(JwtUtil.PREFIX)){
            try {
                String jwt = authToken.substring(JwtUtil.PREFIX.length());
                String jwtAccessToken = JwtUtil.createAccessTokenFromRefreshToken(jwt, request.getRequestURL().toString(), utilisateurService);

                Map<String, String> idToken = new HashMap<>();
                idToken.put("accessToken", jwtAccessToken);
                idToken.put("refreshToken", jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), idToken);
            }catch (Exception e){
                throw e;
            }

        }
        else throw new RuntimeException("Refresh Token Required");
    }


}
