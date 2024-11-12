package com.zenflow.zenflow_back_end_java.controller;

import com.zenflow.zenflow_back_end_java.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://zen-flow-front-end-angular.vercel.app"})
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping("/add-event")
    public String addEventToCalendar(
            @RequestParam String eventTitle,
            @RequestParam LocalDate startDate,
            @RequestParam LocalTime startTime,
            @RequestParam LocalDate endDate,
            @RequestParam LocalTime endTime) {
        return calendarService.addEvent(eventTitle, startDate, startTime, endDate, endTime);
    }
}
