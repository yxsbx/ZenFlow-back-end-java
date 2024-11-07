package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.exception.RateLimitExceededException;
import com.zenflow.zenflow_back_end_java.limiter.RateLimiterService;
import com.zenflow.zenflow_back_end_java.dto.MoodLogDto;
import com.zenflow.zenflow_back_end_java.exception.MoodLogNotFoundException;
import com.zenflow.zenflow_back_end_java.model.MoodLog;
import com.zenflow.zenflow_back_end_java.model.Users;
import com.zenflow.zenflow_back_end_java.repository.MoodLogRepository;
import com.zenflow.zenflow_back_end_java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoodLogService {

    @Autowired
    private MoodLogRepository moodLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RateLimiterService rateLimiterService;

    public List<MoodLogDto> getAllMoodLogs() {
        String key = "mood-log:get-all";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for getting all mood logs. Please try again later.");
        }

        return moodLogRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MoodLogDto createMoodLog(MoodLogDto moodLogDto) {
        String key = "mood-log:create";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for creating mood logs. Please try again later.");
        }

        Users user = userRepository.findById(moodLogDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MoodLog moodLog = new MoodLog();
        moodLog.setUsers(user);
        moodLog.setDate(moodLogDto.getDate());
        moodLog.setMorningMood(moodLogDto.getMorningMood());
        moodLog.setEveningMood(moodLogDto.getEveningMood());

        MoodLog savedMoodLog = moodLogRepository.save(moodLog);
        return convertToDto(savedMoodLog);
    }

    public MoodLogDto getMoodLogById(Long id) {
        String key = "mood-log:get-by-id:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for this mood log. Please try again later.");
        }

        MoodLog moodLog = moodLogRepository.findById(id)
                .orElseThrow(() -> new MoodLogNotFoundException("MoodLog not found with id " + id));
        return convertToDto(moodLog);
    }

    public MoodLogDto updateMoodLog(Long id, MoodLogDto moodLogDto) {
        String key = "mood-log:update:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for updating this mood log. Please try again later.");
        }

        MoodLog moodLog = moodLogRepository.findById(id)
                .orElseThrow(() -> new MoodLogNotFoundException("MoodLog not found with id " + id));

        Users user = userRepository.findById(moodLogDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        moodLog.setUsers(user);
        moodLog.setDate(moodLogDto.getDate());
        moodLog.setMorningMood(moodLogDto.getMorningMood());
        moodLog.setEveningMood(moodLogDto.getEveningMood());
        moodLog.setUpdatedAt(LocalDateTime.now());

        MoodLog updatedMoodLog = moodLogRepository.save(moodLog);
        return convertToDto(updatedMoodLog);
    }

    public void deleteMoodLog(Long id) {
        String key = "mood-log:delete:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for deleting this mood log. Please try again later.");
        }

        MoodLog moodLog = moodLogRepository.findById(id)
                .orElseThrow(() -> new MoodLogNotFoundException("MoodLog not found with id " + id));
        moodLog.setDeletedAt(LocalDateTime.now());
        moodLogRepository.save(moodLog);
    }

    private MoodLogDto convertToDto(MoodLog moodLog) {
        return new MoodLogDto(
                moodLog.getId(),
                moodLog.getUsers().getId(),
                moodLog.getDate(),
                moodLog.getMorningMood(),
                moodLog.getEveningMood()
        );
    }
}
