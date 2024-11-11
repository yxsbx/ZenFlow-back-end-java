package com.zenflow.zenflow_back_end_java.controller;

import com.zenflow.zenflow_back_end_java.dto.LoginRequest;
import com.zenflow.zenflow_back_end_java.dto.TokenResponse;
import com.zenflow.zenflow_back_end_java.service.FirebaseAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://zen-flow-front-end-angular.vercel.app"})
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    /**
     * Endpoint para login.
     *
     * @param loginRequest Dados de login
     * @return TokenResponse com o ID Token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            TokenResponse tokenResponse = firebaseAuthService.login(loginRequest);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            System.err.println("Erro no login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login falhou: " + e.getMessage());
        }
    }

    /**
     * Endpoint para registro.
     *
     * @param loginRequest Dados de registro
     * @return TokenResponse com o ID Token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            TokenResponse tokenResponse = firebaseAuthService.register(loginRequest);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            System.err.println("Erro no registro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registro falhou: " + e.getMessage());
        }
    }
}
