package com.zenflow.zenflow_back_end_java.repository;

import com.zenflow.zenflow_back_end_java.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByFirebaseUidAndDeletedAtIsNull(String firebaseUid);
    Optional<Users> findByIdAndDeletedAtIsNull(Long id);
    List<Users> findAllByDeletedAtIsNull();
}
