package com.zenflow.zenflow_back_end_java.controller;

import com.zenflow.zenflow_back_end_java.dto.RoutineDto;
import com.zenflow.zenflow_back_end_java.service.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @GetMapping
    public List<RoutineDto> getAllRoutines() {
        return routineService.getAllRoutines();
    }

    @PostMapping
    public RoutineDto createRoutine(@RequestBody RoutineDto routineDto) {
        return routineService.createRoutine(routineDto);
    }

    @GetMapping("/{id}")
    public RoutineDto getRoutineById(@PathVariable Long id) {
        return routineService.getRoutineById(id);
    }

    @PutMapping("/{id}")
    public RoutineDto updateRoutine(@PathVariable Long id, @RequestBody RoutineDto routineDto) {
        return routineService.updateRoutine(id, routineDto);
    }

    @DeleteMapping("/{id}")
    public void deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
    }
}
