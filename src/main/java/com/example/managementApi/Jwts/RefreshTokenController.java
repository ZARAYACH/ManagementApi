package com.example.managementApi.Jwts;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/token")
public class RefreshTokenController {

    private final JwtsService jwtsService;

    @PostMapping(path = "/refresh")
    public void getAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        jwtsService.getAccessTokenByRefreshToken(request,response);
        }
}
