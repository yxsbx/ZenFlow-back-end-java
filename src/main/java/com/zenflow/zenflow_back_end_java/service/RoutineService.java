package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.exception.RateLimitExceededException;
import com.zenflow.zenflow_back_end_java.limiter.RateLimiterService;
import com.zenflow.zenflow_back_end_java.dto.RoutineDto;
import com.zenflow.zenflow_back_end_java.exception.RoutineNotFoundException;
import com.zenflow.zenflow_back_end_java.model.Routine;
import com.zenflow.zenflow_back_end_java.model.Users;
import com.zenflow.zenflow_back_end_java.repository.RoutineRepository;
import com.zenflow.zenflow_back_end_java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RateLimiterService rateLimiterService;

    public List<RoutineDto> getAllRoutines() {
        String key = "routine:get-all";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for fetching all routines. Please try again later.");
        }

        return routineRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RoutineDto createRoutine(RoutineDto routineDto) {
        String key = "routine:create";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for creating routines. Please try again later.");
        }

        Users user = userRepository.findById(routineDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Routine routine = new Routine();
        routine.setUsers(user);
        routine.setDate(routineDto.getDate());
        routine.setGoals(routineDto.getGoals());
        routine.setCompleted(routineDto.getCompleted());

        Routine savedRoutine = routineRepository.save(routine);
        return convertToDto(savedRoutine);
    }

    public RoutineDto getRoutineById(Long id) {
        String key = "routine:get-by-id:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for this routine. Please try again later.");
        }

        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RoutineNotFoundException("Routine not found with id " + id));
        return convertToDto(routine);
    }

    public RoutineDto updateRoutine(Long id, RoutineDto routineDto) {
        String key = "routine:update:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for updating this routine. Please try again later.");
        }

        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RoutineNotFoundException("Routine not found with id " + id));

        Users user = userRepository.findById(routineDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        routine.setUsers(user);
        routine.setDate(routineDto.getDate());
        routine.setGoals(routineDto.getGoals());
        routine.setCompleted(routineDto.getCompleted());
        routine.setUpdatedAt(LocalDateTime.now());

        Routine updatedRoutine = routineRepository.save(routine);
        return convertToDto(updatedRoutine);
    }

    public void deleteRoutine(Long id) {
        String key = "routine:delete:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for deleting this routine. Please try again later.");
        }

        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RoutineNotFoundException("Routine not found with id " + id));
        routine.setDeletedAt(LocalDateTime.now());
        routineRepository.save(routine);
    }

    private RoutineDto convertToDto(Routine routine) {
        return new RoutineDto(
                routine.getId(),
                routine.getUsers().getId(),
                routine.getDate(),
                routine.getGoals(),
                routine.getCompleted()
        );
    }
}
