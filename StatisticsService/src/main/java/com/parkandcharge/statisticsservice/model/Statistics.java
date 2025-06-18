package com.parkandcharge.statisticsservice.model;
import lombok.Data;

@Data
public class Statistics {
    private Long stationId;
    private int totalBookings;
    private double totalEarnings;

    // Constructors, Getters, Setters
}
