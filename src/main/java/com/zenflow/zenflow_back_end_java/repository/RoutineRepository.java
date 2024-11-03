package com.zenflow.zenflow_back_end_java.repository;

import com.zenflow.zenflow_back_end_java.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
