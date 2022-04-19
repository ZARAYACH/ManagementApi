package com.example.managementApi.Verification;

import com.example.managementApi.UserCredentiels.UserCredentials;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1")
public class VerificationController {

    private final VerificationTokenService verificationTokenService;

    @GetMapping(path = "/verifyAccount")
    public ResponseEntity<?> verifyAccount(@RequestParam String token,@RequestParam String email){
        return verificationTokenService.verifyAnAccount(token,email);
    }

    @PostMapping(path ="/verifyAccount/changePassword") ResponseEntity<?> setPasswordForNewUser(@RequestParam String token,@RequestBody UserCredentials userCredentials){
        return verificationTokenService.setPasswordForNewUser(token,userCredentials);
    }
}
