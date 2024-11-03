package com.zenflow.zenflow_back_end_java.service;

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

    public List<MoodLogDto> getAllMoodLogs() {
        return moodLogRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MoodLogDto createMoodLog(MoodLogDto moodLogDto) {
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
        MoodLog moodLog = moodLogRepository.findById(id)
                .orElseThrow(() -> new MoodLogNotFoundException("MoodLog not found with id " + id));
        return convertToDto(moodLog);
    }

    public MoodLogDto updateMoodLog(Long id, MoodLogDto moodLogDto) {
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
