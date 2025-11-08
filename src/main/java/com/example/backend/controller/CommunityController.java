package com.example.backend.controller;

import com.example.backend.model.Community;
import com.example.backend.repository.CommunityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communities")
public class CommunityController {

    private final CommunityRepository communityRepository;

    public CommunityController(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    @GetMapping
    public List<Community> getAll() {
        return communityRepository.findAll();
    }

    @PostMapping
    public Community create(@RequestBody Community community) {
        return communityRepository.save(community);
    }
}
