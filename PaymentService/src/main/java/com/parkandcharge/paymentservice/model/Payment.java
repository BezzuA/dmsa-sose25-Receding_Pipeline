package com.parkandcharge.paymentservice.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long bookingId;
    private double amount;
    private String status; // e.g., "PAID", "FAILED"
    private LocalDateTime paymentTime;

    // Getters and Setters
}
