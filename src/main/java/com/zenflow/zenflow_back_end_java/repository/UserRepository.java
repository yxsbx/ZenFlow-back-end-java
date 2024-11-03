package com.zenflow.zenflow_back_end_java.repository;

import com.zenflow.zenflow_back_end_java.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
