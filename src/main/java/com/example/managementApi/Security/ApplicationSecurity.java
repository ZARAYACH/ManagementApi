package com.example.managementApi.Security;


import com.example.managementApi.UserCredentiels.UserCredentialsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.managementApi.User.UserRole.*;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    private final CustomAuthenticationSuccessHandler onAuthenticationSuccessHandler;
    private final UserCredentialsService userCredentialsService;
    private final BCryptPasswordEncoder passwordEncoder ;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/api/v1/supervisor/**)").hasRole(SUPERVISOR.name())
                    .antMatchers("/api/v1/employee/**").hasRole(EMPLOYEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .usernameParameter("email")
                    .permitAll()
                    .defaultSuccessUrl("/api/v1")
                    .successHandler(onAuthenticationSuccessHandler)
                .and()
                .logout().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userCredentialsService);
        return provider;
    }
}
