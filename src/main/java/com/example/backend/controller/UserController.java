package com.example.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.UserDTO;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    // Pasta local onde os arquivos serão salvos (relativa ao diretório de execução)
    private final Path uploadBase = Paths.get("uploads/avatars");

    private void ensureUploadDir() throws IOException {
        if (!Files.exists(uploadBase)) {
            Files.createDirectories(uploadBase);
        }
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
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getAvatarUrl()))
                .collect(Collectors.toList());
    }

    // ========================
    // Buscar usuário por ID
    // ========================
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getAvatarUrl())))
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
        .body(new UserDTO(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getAvatarUrl()));
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
            user.setAvatarUrl(userDetails.getAvatarUrl());

            if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            User updated = repo.save(user);
            return ResponseEntity.ok(new UserDTO(updated.getId(), updated.getUsername(), updated.getEmail(), updated.getAvatarUrl()));
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

        return ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getAvatarUrl()));
    }

    // ========================
    // Upload de avatar (multipart)
    // ========================
    @PostMapping("/{id}/avatar")
    public ResponseEntity<UserDTO> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<User> userOpt = repo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            ensureUploadDir();

            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            String filename = UUID.randomUUID().toString() + ext;
            Path target = uploadBase.resolve(filename).normalize();

            logger.info("Salvando avatar para usuário {} em {}", id, target.toAbsolutePath());

            // grava o arquivo
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // atualiza usuário
            User user = userOpt.get();
            String publicPath = "/uploads/avatars/" + filename; // caminho público para servir
            user.setAvatarUrl(publicPath);
            User updated = repo.save(user);

            logger.info("Imagem salva com sucesso: {} (usuario id={})", filename, id);

            return ResponseEntity.ok(new UserDTO(updated.getId(), updated.getUsername(), updated.getEmail(), updated.getAvatarUrl()));
        } catch (IOException e) {
            logger.error("Erro ao salvar imagem para usuario {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
