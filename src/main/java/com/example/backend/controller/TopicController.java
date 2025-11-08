package com.example.backend.controller;

import com.example.backend.model.Topic;
import com.example.backend.repository.TopicRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicRepository topicRepository;

    public TopicController(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @GetMapping
    public List<Topic> getAll() {
        return topicRepository.findAll();
    }

    @PostMapping
    public Topic create(@RequestBody Topic topic) {
        return topicRepository.save(topic);
    }
}
