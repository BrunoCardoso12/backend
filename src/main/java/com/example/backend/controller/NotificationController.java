package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.model.Notification;
import com.example.backend.repository.NotificationRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/for/{userId}")
    public ResponseEntity<List<NotificationDTO>> forUser(@PathVariable Long userId) {
        List<Notification> list = repo.findByUserIdOrderByCreatedAtDesc(userId);
        List<NotificationDTO> dtos = list.stream().map(n -> new NotificationDTO(n.getId(), n.getType(), n.getTitle(), n.getBody(), n.getCreatedAt(), n.isReadFlag())).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markRead(@PathVariable Long id) {
        return repo.findById(id).map(n -> {
            n.setReadFlag(true);
            Notification saved = repo.save(n);
            NotificationDTO dto = new NotificationDTO(saved.getId(), saved.getType(), saved.getTitle(), saved.getBody(), saved.getCreatedAt(), saved.isReadFlag());
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }
}
