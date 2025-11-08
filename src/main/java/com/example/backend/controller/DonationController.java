package com.example.backend.controller;

import com.example.backend.model.Donation;
import com.example.backend.repository.DonationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donations")
public class DonationController {

    private final DonationRepository donationRepository;

    public DonationController(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @GetMapping
    public List<Donation> getAll() {
        return donationRepository.findAll();
    }

    @PostMapping
    public Donation create(@RequestBody Donation donation) {
        return donationRepository.save(donation);
    }
}
