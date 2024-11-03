package com.zenflow.zenflow_back_end_java.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            return ResponseEntity.ok("Login bem-sucedido. UID: " + uid);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
        }
    }
}