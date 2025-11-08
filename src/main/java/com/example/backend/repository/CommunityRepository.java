package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    // Adicione métodos específicos se necessário
}
