package com.parkandcharge.statisticsservice.model;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class Statistics {
    @Id
    private Long ownerId;

    @Column(nullable = false)
    private int totalBookings;

    @Column(nullable = false)
    private double totalEarnings;

    public Statistics() {}
    public Statistics(Long ownerId, int totalBookings, double totalEarnings) {
        this.ownerId = ownerId;
        this.totalBookings = totalBookings;
        this.totalEarnings = totalEarnings;
    }

    // Constructors, Getters, Setters
}
