package com.parkandcharge.bookingservice.repository;
import com.parkandcharge.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStationIdAndStatus(Long stationId, String status);
    List<Booking> findByStationIdIn(List<Long> stationIds);

    @Query("SELECT b.stationId FROM Booking b WHERE b.status <> 'CANCELLED' AND " +
            "((b.startTime < :end) AND (b.endTime > :start))")
    List<Long> findConflictingStationIds(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.stationId = :stationId AND b.status = 'IN_USE' AND b.startTime <= :now AND b.endTime >= :now")
    boolean existsInUse(@Param("stationId") Long stationId, @Param("now") LocalDateTime now);
}