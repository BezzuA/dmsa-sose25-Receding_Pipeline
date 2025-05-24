package com.parkandcharge.bookingservice.repository;
import com.parkandcharge.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStationIdAndStatus(Long stationId, String status);
}