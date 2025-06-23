package com.parkandcharge.bookingservice.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // FK to User
    private Long stationId; // FK to Station
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double amount;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BookingStatus status = BookingStatus.PENDING;
}