package com.parkandcharge.chargingservice.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Charging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long ownerId; // User ID of the OWNER

    private Double pricePerMinute;

    private double latitude;
    private double longitude;

    private LocalDateTime availableFrom;
    private LocalDateTime availableUntil;
}
