package com.example.managementApi.Security;

import com.example.managementApi.User.User;
import com.example.managementApi.User.UserRepo;
import com.example.managementApi.signInLogs.Logs;
import com.example.managementApi.signInLogs.LogsRepo;
import com.example.managementApi.signInLogs.LogsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LogsRepo logsRepo;
    private final UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = userRepo.getUserByEmail(authentication.getName());
        var details = (WebAuthenticationDetails)authentication.getDetails();
        request.getSession().setAttribute("user",user);
        Logs log = new Logs(LocalDateTime.now(),null,
               details.getRemoteAddress(),
                user);        logsRepo.save(log);

    }
}
