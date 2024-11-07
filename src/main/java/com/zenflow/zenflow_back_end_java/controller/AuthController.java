package com.zenflow.zenflow_back_end_java.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.zenflow.zenflow_back_end_java.dto.AuthRequestDto;
import com.zenflow.zenflow_back_end_java.limiter.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequestDto authRequest) {
        String key = authRequest.getIdToken();

        if (loginAttemptService.isBlocked(key)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Too many failed login attempts. Please try again later.");
            return ResponseEntity.status(429).body(errorResponse);
        }

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authRequest.getIdToken());
            String uid = decodedToken.getUid();

            loginAttemptService.loginSucceeded(key);

            Map<String, String> response = new HashMap<>();
            response.put("uid", uid);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (FirebaseAuthException e) {
            loginAttemptService.loginFailed(key);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
