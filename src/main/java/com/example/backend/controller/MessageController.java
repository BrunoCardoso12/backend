package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.MessageDTO;
import com.example.backend.dto.NotificationDTO;
import com.example.backend.model.Message;
import com.example.backend.model.Notification;
import com.example.backend.repository.MessageRepository;
import com.example.backend.repository.NotificationRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository repo;
    private final NotificationRepository notificationRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageRepository repo, NotificationRepository notificationRepo, SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.notificationRepo = notificationRepo;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public ResponseEntity<MessageDTO> create(@RequestBody MessageDTO dto) {
        if (dto.getSenderId() == null || dto.getReceiverId() == null || dto.getContent() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Message m = new Message(dto.getSenderId(), dto.getReceiverId(), dto.getContent());
        Message saved = repo.save(m);
        MessageDTO out = new MessageDTO(saved.getId(), saved.getSenderId(), saved.getReceiverId(), saved.getContent(), saved.getCreatedAt());

        // Send real-time update to conversation topic (use sorted ids to form channel)
        Long a = saved.getSenderId();
        Long b = saved.getReceiverId();
        String conversationId = (a < b) ? (a + "_" + b) : (b + "_" + a);
        try {
            messagingTemplate.convertAndSend("/topic/conversation." + conversationId, out);
            // also create and persist a notification and notify the receiver directly
            try {
                Notification notif = new Notification(saved.getReceiverId(), "message", "Nova mensagem de " + saved.getSenderId(), saved.getContent());
                Notification savedNotif = notificationRepo.save(notif);
                NotificationDTO note = new NotificationDTO(savedNotif.getId(), savedNotif.getType(), savedNotif.getTitle(), savedNotif.getBody(), savedNotif.getCreatedAt(), savedNotif.isReadFlag());
                messagingTemplate.convertAndSendToUser(String.valueOf(saved.getReceiverId()), "/queue/notifications", note);
            } catch (Exception e) {
                System.err.println("Failed to send notification: " + e.getMessage());
            }
        } catch (Exception ex) {
            // log if needed (keeping controller lightweight)
            System.err.println("Failed to send STOMP message: " + ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @GetMapping("/conversation/{a}/{b}")
    public ResponseEntity<List<MessageDTO>> conversation(@PathVariable Long a, @PathVariable Long b) {
        List<Message> list = repo.findConversation(a, b);
        List<MessageDTO> dtos = list.stream().map(m -> new MessageDTO(m.getId(), m.getSenderId(), m.getReceiverId(), m.getContent(), m.getCreatedAt())).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/for/{userId}")
    public ResponseEntity<List<MessageDTO>> forUser(@PathVariable Long userId) {
        List<Message> list = repo.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
        List<MessageDTO> dtos = list.stream().map(m -> new MessageDTO(m.getId(), m.getSenderId(), m.getReceiverId(), m.getContent(), m.getCreatedAt())).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
