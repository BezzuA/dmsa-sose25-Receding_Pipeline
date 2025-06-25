package com.parkandcharge.bookingservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChargingDto {
    private Long id;
    private String name;
    private Double pricePerMinute;
    private Double latitude;
    private Double longitude;
    private Long ownerId;
    private LocalDateTime availableFrom;
    private LocalDateTime availableUntil;
}