package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.dto.UserDto;
import com.zenflow.zenflow_back_end_java.exception.UserNotFoundException;
import com.zenflow.zenflow_back_end_java.model.Users;
import com.zenflow.zenflow_back_end_java.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        return convertToDto(user);
    }

    public UserDto findOrCreateUserByFirebaseUid(String firebaseUid, String name, String email) {
        Optional<Users> existingUser = userRepository.findByFirebaseUid(firebaseUid);

        if (existingUser.isPresent()) {
            logger.info("Usuário existente encontrado: UID {}", firebaseUid);
            return convertToDto(existingUser.get());
        } else {
            logger.info("Criando novo usuário: UID {}", firebaseUid);
            Users newUser = new Users();
            newUser.setFirebaseUid(firebaseUid);
            newUser.setName(name);
            newUser.setEmail(email);

            Users savedUser = userRepository.save(newUser);
            logger.info("Usuário criado com ID {}", savedUser.getId());//
            return convertToDto(savedUser);
        }
    }

    public UserDto updateUser(Long id, UserDto userDetails) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        if (userDetails.getName() != null) user.setName(userDetails.getName());
        if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
        if (userDetails.getFirebaseUid() != null) user.setFirebaseUid(userDetails.getFirebaseUid());

        Users updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private UserDto convertToDto(Users user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getFirebaseUid(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                null
        );
    }
}
