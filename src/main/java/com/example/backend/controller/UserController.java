package com.example.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.UserDTO;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        return create(user); // Reaproveita o método existente
    }

    // ========================
    // Listar todos os usuários
    // ========================
    @GetMapping
    public List<UserDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    // ========================
    // Buscar usuário por ID
    // ========================
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername(), user.getEmail())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ========================
    // Criar novo usuário
    // ========================
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDTO(saved.getId(), saved.getUsername(), saved.getEmail()));
    }

    // ========================
    // Atualizar usuário
    // ========================
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOpt = repo.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());

            if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            User updated = repo.save(user);
            return ResponseEntity.ok(new UserDTO(updated.getId(), updated.getUsername(), updated.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        Optional<User> userOpt = repo.findByEmail(loginData.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("E-mail não encontrado");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Senha incorreta");
        }

        return ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
    }

    // ========================
    // Deletar usuário
    // ========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
