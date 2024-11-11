package com.zenflow.zenflow_back_end_java.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String firebaseUid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String idToken;
}
