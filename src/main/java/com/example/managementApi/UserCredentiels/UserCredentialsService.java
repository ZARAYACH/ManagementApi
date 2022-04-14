package com.example.managementApi.UserCredentiels;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserCredentialsService implements UserDetailsService {

    private final UserCredentialsRepo userCredentialsRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userCredentialsRepo.findByEmail(email)==null){
            throw new UsernameNotFoundException("this user not found");
        }else {
            UserCredentials userCredentials = userCredentialsRepo.findByEmail(email);
            return new User(userCredentials.getUsername(), userCredentials.getPassword(),userCredentials.getAuthorities());
        }
    }


    public boolean cheekStrongestOfPassword(UserCredentials userCredentials){
        String password = userCredentials.getPassword();
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
