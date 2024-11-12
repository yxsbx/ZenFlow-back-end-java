package com.zenflow.zenflow_back_end_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String idToken;
    private String customToken;

    public TokenResponse(String customToken) {
        this.customToken = customToken;
    }
}
