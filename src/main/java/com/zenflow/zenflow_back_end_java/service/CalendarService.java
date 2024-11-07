package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.exception.RateLimitExceededException;
import com.zenflow.zenflow_back_end_java.limiter.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CalendarService {

    @Autowired
    private Calendar googleCalendarService;

    @Autowired
    private RateLimiterService rateLimiterService;

    public String addEvent(String eventTitle, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String key = "calendar:add-event";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for adding events. Please try again later.");
        }

        try {
            Event event = new Event()
                    .setSummary(eventTitle);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                    .setTimeZone(TimeZone.getDefault().getID());

            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                    .setTimeZone(TimeZone.getDefault().getID());

            event.setStart(start);
            event.setEnd(end);

            Event createdEvent = googleCalendarService.events().insert("primary", event).execute();
            return "Event created: " + createdEvent.getHtmlLink();
        } catch (IOException e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}
