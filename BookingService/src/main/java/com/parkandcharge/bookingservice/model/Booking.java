package com.parkandcharge.bookingservice.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // FK to User
    private Long stationId; // FK to Station
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double amount;
    private String status; // e.g., "BOOKED", "CANCELLED"

    // Getters & Setters
}