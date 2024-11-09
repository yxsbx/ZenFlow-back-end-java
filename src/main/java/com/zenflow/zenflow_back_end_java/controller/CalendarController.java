package com.zenflow.zenflow_back_end_java.controller;

import com.zenflow.zenflow_back_end_java.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping("/add-event")
    public String addEventToCalendar(
            @RequestParam String eventTitle,
            @RequestParam LocalDateTime startDateTime,
            @RequestParam LocalDateTime endDateTime) {
        return calendarService.addEvent(eventTitle, startDateTime, endDateTime);
    }
}
