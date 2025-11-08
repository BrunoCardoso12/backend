package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonorId(Long donorId);     // assumes Donation has User donor
    List<Donation> findByReceiverId(Long receiverId); // assumes Donation has User receiver
}
