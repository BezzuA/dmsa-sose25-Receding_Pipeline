// BookingStatus.java
package com.parkandcharge.bookingservice.model;

public enum BookingStatus {
    PENDING,      // Booking created, waiting for owner approval
    APPROVED,     // Booking approved by owner, waiting for start time
    IN_USE,       // Booking is currently in progress (started, not finished)
    COMPLETED,    // Booking finished
    CANCELLED,    // Booking was cancelled
    FAILED        // Booking failed (e.g., payment or other issues)
}
