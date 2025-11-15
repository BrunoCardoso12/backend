package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.TopicDTO;
import com.example.backend.model.Topic;
import com.example.backend.service.TopicService;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/book/{bookId}")
    public List<TopicDTO> getTopicsByBookId(@PathVariable Long bookId) {
        return topicService.findByBookId(bookId)
                .stream()
                .map(TopicDTO::new)
                .collect(Collectors.toList());
    }
}
