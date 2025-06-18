package com.parkandcharge.chargingservice.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Charging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private boolean inUse;
    private Long ownerId; // User who owns the station

    // Getters and Setters
}
