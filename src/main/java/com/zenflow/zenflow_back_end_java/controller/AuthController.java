package com.zenflow.zenflow_back_end_java.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.zenflow.zenflow_back_end_java.dto.UserDto;
import com.zenflow.zenflow_back_end_java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://zen-flow-front-end-angular.vercel.app"})
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginOrRegister(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String name) {

        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            UserRecord userRecord;

            try {
                userRecord = firebaseAuth.getUserByEmail(email);
            } catch (FirebaseAuthException e) {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setEmail(email)
                        .setPassword(password)
                        .setDisplayName(name != null ? name : email.split("@")[0]);
                userRecord = firebaseAuth.createUser(request);
            }

            String idToken = firebaseAuth.createCustomToken(userRecord.getUid());

            UserDto userDto = userService.findOrCreateUserByFirebaseUid(userRecord.getUid(), userRecord.getDisplayName(), email);
            userDto.setIdToken(idToken);

            return ResponseEntity.ok(userDto);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
