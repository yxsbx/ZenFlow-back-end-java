package com.zenflow.zenflow_back_end_java.service;

import com.google.firebase.auth.*;
import com.zenflow.zenflow_back_end_java.dto.LoginRequest;
import com.zenflow.zenflow_back_end_java.dto.TokenResponse;
import com.zenflow.zenflow_back_end_java.dto.UserDto;
import com.zenflow.zenflow_back_end_java.exception.AuthenticationException;
import com.zenflow.zenflow_back_end_java.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class FirebaseAuthService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthService.class);

    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    @Autowired
    private UserService userService;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private final RestTemplate restTemplate = new RestTemplate();

    public TokenResponse register(LoginRequest loginRequest) throws RegistrationException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String name = loginRequest.getName();

        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(name != null ? name : email.split("@")[0]);

            logger.info("Criando novo usuário no Firebase: {}", email);

            UserRecord userRecord = firebaseAuth.createUser(request);
            String uid = userRecord.getUid();
            String displayName = userRecord.getDisplayName();
            String emailResponse = userRecord.getEmail();

            logger.info("Usuário criado no Firebase: UID {}", uid);

            userService.findOrCreateUserByFirebaseUid(uid, displayName, emailResponse);

            String customToken = firebaseAuth.createCustomToken(uid);

            // Passar null para idToken
            return new TokenResponse(null, customToken);
        } catch (FirebaseAuthException e) {
            logger.error("Erro ao registrar usuário: {}", e.getMessage());
            throw new RegistrationException("Erro ao registrar usuário: " + e.getMessage(), e);
        }
    }

    public TokenResponse login(LoginRequest loginRequest) throws AuthenticationException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey;

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        payload.put("returnSecureToken", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                String idToken = (String) responseBody.get("idToken");
                return new TokenResponse(idToken, null);
            } else {
                logger.error("Erro no login: {}", response.getBody());
                throw new AuthenticationException("Login falhou");
            }
        } catch (Exception e) {
            logger.error("Erro no login: {}", e.getMessage());
            throw new AuthenticationException("Login falhou: " + e.getMessage(), e);
        }
    }

    public List<UserDto> listAllUsers() throws FirebaseAuthException {
        List<UserDto> users = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ListUsersPage page = auth.listUsers(null);
        for (ExportedUserRecord user : page.iterateAll()) {
            users.add(new UserDto(
                    null,
                    user.getDisplayName(),
                    user.getEmail(),
                    user.getUid(),
                    null,
                    null
            ));
        }
        return users;
    }
}
