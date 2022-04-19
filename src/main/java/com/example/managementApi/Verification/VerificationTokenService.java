package com.example.managementApi.Verification;

import com.example.managementApi.MailSender.JavaEmailSenderService;
import com.example.managementApi.User.User;
import com.example.managementApi.User.UserRepo;
import com.example.managementApi.UserCredentiels.UserCredentials;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class VerificationTokenService {

    private final UserRepo userRepo;
    private final JavaEmailSenderService emailSenderService;
    private final BCryptPasswordEncoder passwordEncoder;

    public String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public ResponseEntity<?> verifyAnAccount(String token, String email) {
        if (userRepo.existsByEmail(email) != null) {
            User user = userRepo.getUserByEmail(email);
            if (!user.isActive()) {
                String verificationToken = user.getUserCredentials().getVerficationToken();
                if (verificationToken == token) {
                    return ResponseEntity.ok().body("this account needs new password");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("this token is invalid please check your inbox");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("this User is already active");
            }

        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> ResendTheToken(String email) {
        if (userRepo.existsByEmail(email) != null) {
            User user = userRepo.getUserByEmail(email);
            if (!user.isActive()) {
                user.getUserCredentials().setVerficationToken(generateVerificationToken());
                String link = "http://localhost:8080/api/v1/verifyAccount?token=" + user.getUserCredentials().getVerficationToken() + "email=" + user.getEmail();
                emailSenderService.SendHtmlEmail(user.getEmail(),
                        "mohamedachbani50@gmail.com",
                        "Please verified your account ",
                        "<h1>verified your cprofile management profile</h1" +
                                "<p>Please click <a href=" + link + ">HERE</a>to verified " + " your account </p>");
                return ResponseEntity.ok().body("the verification link has been resend");
            } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("this user is already verified");
        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> setPasswordForNewUser(String token, UserCredentials userCredentials) {
        if (userRepo.existsById(userCredentials.getUser().getId())){
            User user = userRepo.getUserByEmail(userCredentials.getEmail());
            if (user.getUserCredentials().getVerficationToken() == token){
                user.getUserCredentials().setPassword(passwordEncoder.encode(userCredentials.getPassword()));
                user.setActive(true);
                userRepo.save(user);
                return ResponseEntity.ok().body("your account now activated and you can login with your credentials");
            }else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("the token is invalid");
        }else return ResponseEntity.notFound().build();
    }
}
