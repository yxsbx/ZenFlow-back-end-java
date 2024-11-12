package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.dto.RoutineDto;
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
        Users user = findUserById(routineDto.getUserId());

        Routine routine = new Routine();
        routine.setUsers(user);
        routine.setStartDate(routineDto.getStartDate());
        routine.setStartTime(routineDto.getStartTime());
        routine.setEndDate(routineDto.getEndDate());
        routine.setEndTime(routineDto.getEndTime());
        routine.setGoals(routineDto.getGoals());
        routine.setCompleted(routineDto.getCompleted());
        routine.setSendToCalendar(routineDto.getSendToCalendar());

        Routine savedRoutine = routineRepository.save(routine);
        return convertToDto(savedRoutine);
    }

    public RoutineDto updateRoutine(Long id, RoutineDto routineDto) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routine not found with id " + id));

        Users user = findUserById(routineDto.getUserId());

        routine.setUsers(user);
        routine.setStartDate(routineDto.getStartDate());
        routine.setStartTime(routineDto.getStartTime());
        routine.setEndDate(routineDto.getEndDate());
        routine.setEndTime(routineDto.getEndTime());
        routine.setGoals(routineDto.getGoals());
        routine.setCompleted(routineDto.getCompleted());
        routine.setUpdatedAt(LocalDateTime.now());

        Routine updatedRoutine = routineRepository.save(routine);
        return convertToDto(updatedRoutine);
    }

    public RoutineDto getRoutineById(Long id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routine not found with id " + id));
        return convertToDto(routine);
    }

    public void deleteRoutine(Long id) {
        Optional<Routine> routine = routineRepository.findById(id);
        routine.ifPresent(r -> {
            r.setDeletedAt(LocalDateTime.now());
            routineRepository.save(r);
        });
    }

    private RoutineDto convertToDto(Routine routine) {
        return new RoutineDto(
                routine.getId(),
                routine.getUsers().getId(),
                routine.getStartDate(),
                routine.getStartTime(),
                routine.getEndDate(),
                routine.getEndTime(),
                routine.getGoals(),
                routine.getCompleted(),
                routine.getSendToCalendar()
        );
    }

    private Users findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
    }
}
