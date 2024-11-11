package com.zenflow.zenflow_back_end_java.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.zenflow.zenflow_back_end_java.dto.LoginRequest;
import com.zenflow.zenflow_back_end_java.dto.TokenResponse;
import com.zenflow.zenflow_back_end_java.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseAuthService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthService.class);

    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    @Autowired
    private UserService userService;

    private final RestTemplate restTemplate = new RestTemplate();

    public TokenResponse login(LoginRequest loginRequest) throws Exception {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("returnSecureToken", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey;

        logger.info("Enviando requisição de login para o Firebase");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            String idToken = (String) responseBody.get("idToken");
            String uid = (String) responseBody.get("localId");
            String displayName = (String) responseBody.get("displayName");
            String emailResponse = (String) responseBody.get("email");

            logger.info("Usuário autenticado com sucesso: UID {}", uid);

            UserDto userDto = userService.findOrCreateUserByFirebaseUid(uid, displayName, emailResponse);
            userDto.setIdToken(idToken);

            return new TokenResponse(idToken);
        } else {
            logger.error("Falha na autenticação: Status {}", response.getStatusCode());
            throw new Exception("Falha na autenticação");
        }
    }

    public TokenResponse register(LoginRequest loginRequest) throws Exception {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String name = loginRequest.getName();

        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

            UserDto userDto = userService.findOrCreateUserByFirebaseUid(uid, displayName, emailResponse);

            Map<String, Object> body = new HashMap<>();
            body.put("email", email);
            body.put("password", password);
            body.put("returnSecureToken", true);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> authRequest = new HttpEntity<>(body, headers);

            String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey;

            logger.info("Autenticando usuário recém-criado para obter o idToken");

            ResponseEntity<Map> authResponse = restTemplate.postForEntity(url, authRequest, Map.class);

            if (authResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> authResponseBody = authResponse.getBody();
                String idToken = (String) authResponseBody.get("idToken");
                userDto.setIdToken(idToken);
                logger.info("Autenticação após registro bem-sucedida: idToken gerado");
                return new TokenResponse(idToken);
            } else {
                logger.error("Falha na autenticação após registro: Status {}", authResponse.getStatusCode());
                throw new Exception("Falha na autenticação após registro");
            }

        } catch (FirebaseAuthException e) {
            logger.error("Erro ao registrar usuário: {}", e.getMessage());
            throw new Exception("Erro ao registrar usuário: " + e.getMessage());
        }
    }
}
