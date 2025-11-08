package com.example.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:5173") // Ajuste se seu frontend estiver em outro endereço
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    // Listar todos os usuários
    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable Long id) {
        return repo.findById(id);
    }

    // Criar novo usuário
    @PostMapping
    public User create(@RequestBody User user) {
        return repo.save(user);
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User userDetails) {
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return repo.save(user);
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
