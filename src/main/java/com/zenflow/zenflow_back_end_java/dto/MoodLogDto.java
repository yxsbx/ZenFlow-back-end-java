package com.zenflow.zenflow_back_end_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodLogDto {
    private Long id;
    private Long userId;
    private LocalDate date;
    private String morningMood;
    private String eveningMood;
}
