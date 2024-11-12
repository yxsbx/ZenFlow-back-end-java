package com.zenflow.zenflow_back_end_java.controller;

import com.zenflow.zenflow_back_end_java.dto.LoginRequest;
import com.zenflow.zenflow_back_end_java.dto.TokenResponse;
import com.zenflow.zenflow_back_end_java.exception.RegistrationException;
import com.zenflow.zenflow_back_end_java.exception.AuthenticationException;
import com.zenflow.zenflow_back_end_java.service.FirebaseAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://zen-flow-front-end-angular.vercel.app"})
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            TokenResponse tokenResponse = firebaseAuthService.register(loginRequest);
            return ResponseEntity.ok(tokenResponse);
        } catch (RegistrationException e) {
            logger.error("Erro no registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registro falhou: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro no registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registro falhou: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            TokenResponse tokenResponse = firebaseAuthService.login(loginRequest);
            return ResponseEntity.ok(tokenResponse);
        } catch (AuthenticationException e) {
            logger.error("Erro no login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login falhou: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro no login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login falhou: " + e.getMessage());
        }
    }
}
