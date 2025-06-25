package com.parkandcharge.bookingservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    private Long userId;
    private Long stationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double amount;
}