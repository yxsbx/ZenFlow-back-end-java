package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.exception.RateLimitExceededException;
import com.zenflow.zenflow_back_end_java.limiter.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.TimeZone;

@Service
public class CalendarService {

    @Autowired
    private Calendar googleCalendarService;

    @Autowired
    private RateLimiterService rateLimiterService;

    public String addEvent(String eventTitle, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        String key = "calendar:add-event";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for adding events. Please try again later.");
        }

        try {
            Event event = new Event().setSummary(eventTitle);

            ZoneId zoneId = ZoneId.systemDefault();

            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDate.atTime(startTime).atZone(zoneId).toInstant().toEpochMilli()))
                    .setTimeZone(TimeZone.getDefault().getID());

            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(endDate.atTime(endTime).atZone(zoneId).toInstant().toEpochMilli()))
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
