package com.example.managementApi.Security;


import com.example.managementApi.Jwts.CustomAuthenticationFilter;
import com.example.managementApi.Jwts.CustomAuthorizationFilter;
import com.example.managementApi.UserCredentiels.UserCredentialsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.file.AccessDeniedException;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private final UserCredentialsService userCredentialsService;
    private final BCryptPasswordEncoder passwordEncoder ;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint ;
    private final AccessDeniedHandler accessDeniedExceptionHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(this.restAuthenticationEntryPoint)
                .accessDeniedHandler((AccessDeniedHandler) this.accessDeniedExceptionHandler)
                .and()
                .addFilter(new CustomAuthenticationFilter(authenticationManager()))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/v1/supervisor/**").hasRole("SUPERVISOR")
                .antMatchers("/api/v1/employee/**").hasRole("EMPLOYEE")
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userCredentialsService).passwordEncoder(passwordEncoder);
    }


}
