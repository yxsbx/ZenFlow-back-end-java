package com.zenflow.zenflow_back_end_java.service;

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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private UserRepository userRepository;

    public List<RoutineDto> getAllRoutines() {
        return routineRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RoutineDto createRoutine(RoutineDto routineDto) {
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
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RoutineNotFoundException("Routine not found with id " + id));
        return convertToDto(routine);
    }

    public RoutineDto updateRoutine(Long id, RoutineDto routineDto) {
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
