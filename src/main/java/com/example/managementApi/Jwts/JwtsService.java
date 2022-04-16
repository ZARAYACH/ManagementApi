package com.example.managementApi.Jwts;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.managementApi.UserCredentiels.UserCredentials;
import com.example.managementApi.UserCredentiels.UserCredentialsRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class JwtsService {

    @Autowired
    private UserCredentialsRepo userCredentialsRepo;

    public String createJwtAccessToken(HttpServletRequest request, User user){
        Algorithm algorithmAccess = Algorithm.HMAC256("secretsecretsecretsecretsecretsecretsecret".getBytes(StandardCharsets.UTF_8));
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuedAt(Date.valueOf(LocalDate.now()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithmAccess);
        return access_token;
    }

    public String createJwtRefreshToken(HttpServletRequest request,User user){
        Algorithm algorithmRefresh = Algorithm.HMAC256("refreshrefreshrefreshrefreshrefreshrefreshrefresh".getBytes(StandardCharsets.UTF_8));
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(Date.valueOf(LocalDate.now()))
                .withExpiresAt(Date.valueOf(LocalDate.now().plusMonths(4)))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithmRefresh);
        return refresh_token;
    }

    public void getAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String refreshTokenHeader = request.getHeader("refreshToken");
        if (refreshTokenHeader !=null && refreshTokenHeader.startsWith("Bearer ")){
            String refresh_token =   refreshTokenHeader.substring("Bearer ".length());
            Algorithm algorithmRefresh = Algorithm.HMAC256("refreshrefreshrefreshrefreshrefreshrefreshrefresh".getBytes(StandardCharsets.UTF_8));
            try {
                JWTVerifier verifier = JWT.require(algorithmRefresh).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                UserCredentials userCredentials = userCredentialsRepo.getByEmail(email);
                User user = new User(userCredentials.getEmail(),
                        userCredentials.getPassword(),
                        userCredentials.isEnabled(),
                        userCredentials.isCredentialsNonExpired(),
                        userCredentials.isCredentialsNonExpired(),
                        userCredentials.isAccountNonLocked(),
                        userCredentials.getAuthorities());
                String access_token = createJwtAccessToken(request,user);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch (Exception e){
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                HashMap<String,String> error = new HashMap<>();
                error.put("error",e.getMessage().toString());
                System.out.println(e.getMessage().toString());
                new ObjectMapper().writeValue(response.getOutputStream(),error.toString());
                e.printStackTrace();

            }
        }
    }
}
