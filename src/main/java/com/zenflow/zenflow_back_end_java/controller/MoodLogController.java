package com.zenflow.zenflow_back_end_java.controller;

import com.zenflow.zenflow_back_end_java.dto.MoodLogDto;
import com.zenflow.zenflow_back_end_java.service.MoodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://zen-flow-front-end-angular.vercel.app"})
@RequestMapping("/api/mood-logs")
public class MoodLogController {

    @Autowired
    private MoodLogService moodLogService;

    @GetMapping
    public List<MoodLogDto> getAllMoodLogs() {
        return moodLogService.getAllMoodLogs();
    }

    @PostMapping
    public MoodLogDto createMoodLog(@RequestBody MoodLogDto moodLogDto) {
        return moodLogService.createMoodLog(moodLogDto);
    }

    @GetMapping("/{id}")
    public MoodLogDto getMoodLogById(@PathVariable Long id) {
        return moodLogService.getMoodLogById(id);
    }

    @PutMapping("/{id}")
    public MoodLogDto updateMoodLog(@PathVariable Long id, @RequestBody MoodLogDto moodLogDto) {
        return moodLogService.updateMoodLog(id, moodLogDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMoodLog(@PathVariable Long id) {
        moodLogService.deleteMoodLog(id);
    }
}
