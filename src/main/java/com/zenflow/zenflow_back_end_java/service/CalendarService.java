package com.zenflow.zenflow_back_end_java.service;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Service
public class CalendarService {

    @Autowired
    private Calendar googleCalendarService;

    public String addEvent(String eventTitle, String date) {
        try {
            Event event = new Event()
                    .setSummary(eventTitle);

            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            EventDateTime eventDateTime = new EventDateTime()
                    .setDate(new com.google.api.client.util.DateTime(localDate.toString()))
                    .setTimeZone(TimeZone.getDefault().getID());
            event.setStart(eventDateTime);
            event.setEnd(eventDateTime);

            event = googleCalendarService.events().insert("primary", event).execute();
            return "Event created: " + event.getHtmlLink();
        } catch (IOException e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}
