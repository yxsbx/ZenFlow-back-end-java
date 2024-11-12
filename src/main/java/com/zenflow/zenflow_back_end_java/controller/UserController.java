package com.zenflow.zenflow_back_end_java.controller;

import com.zenflow.zenflow_back_end_java.dto.UserDto;
import com.zenflow.zenflow_back_end_java.exception.UserNotFoundException;
import com.zenflow.zenflow_back_end_java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://zen-flow-front-end-angular.vercel.app"})
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Erro ao obter todos os usuários: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(userDto);
        } catch (UserNotFoundException e) {
            logger.error("Erro ao obter usuário por ID: {}", e.getMessage());
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Erro ao obter usuário por ID: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar usuário: {}", e.getMessage());
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            logger.error("Erro ao deletar usuário: {}", e.getMessage());
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        } catch (Exception e) {
            logger.error("Erro ao deletar usuário: {}", e.getMessage());
            return ResponseEntity.status(500).body("Erro interno ao deletar usuário.");
        }
    }
}
