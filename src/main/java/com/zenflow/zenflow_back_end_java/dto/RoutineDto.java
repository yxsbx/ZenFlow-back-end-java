package com.zenflow.zenflow_back_end_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDto {
    private Long id;
    private Long userId;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String goals;
    private Boolean completed;
    private Boolean sendToCalendar;
}
