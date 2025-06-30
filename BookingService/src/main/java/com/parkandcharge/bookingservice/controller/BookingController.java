package com.parkandcharge.bookingservice.controller;
import com.parkandcharge.bookingservice.dto.BookingRequestDto;
import com.parkandcharge.bookingservice.dto.ChargingDto;
import com.parkandcharge.bookingservice.model.Booking;
import com.parkandcharge.bookingservice.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {
    private final BookingService bookingService;
    private final RestTemplate restTemplate;
    @Value("${charging.service.url:http://charging-service}")
    private String chargingServiceUrl;

    public BookingController(BookingService bookingService, RestTemplate restTemplate) {
        this.bookingService = bookingService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Optional<Booking> getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @PostMapping("/{id}/cancel")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @PostMapping("/{id}/approve")
    public Booking approveBooking(@PathVariable Long id) {
        return bookingService.approveBooking(id);
    }

    @PostMapping("/{id}/start")
    public Booking startBooking(@PathVariable Long id) {
        return bookingService.startBooking(id);
    }

    @PostMapping("/{id}/complete")
    public Booking completeBooking(@PathVariable Long id) {
        return bookingService.completeBooking(id);
    }

    @GetMapping("/available")
    public List<ChargingDto> getAvailableStations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return bookingService.getAvailableStations(start, end);
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> bookStation(@RequestBody BookingRequestDto request) {
        return ResponseEntity.ok(bookingService.bookStation(request));
    }

    @GetMapping("/owner-bookings")
    public List<Booking> getBookingsForOwner(@RequestParam Long ownerId) {
        String url = chargingServiceUrl + "/api/charging/owner/charging-ids?ownerId=" + ownerId;
        List<Integer> chargingIds = restTemplate.getForObject(url, List.class);
        List<Long> stationIds = chargingIds == null ? List.of() : chargingIds.stream().map(Integer::longValue).toList();
        return bookingService.getBookingsByStationIds(stationIds);
    }

    @GetMapping("/station/{stationId}/in-use")
    public boolean isStationInUse(@PathVariable Long stationId) {
        return bookingService.isStationInUse(stationId);
    }

    @GetMapping("/in-use-station-ids")
    public List<Long> getInUseStationIds() {
        LocalDateTime now = LocalDateTime.now();
        return bookingService.getAllBookings().stream()
            .filter(b -> b.getStatus() == com.parkandcharge.bookingservice.model.BookingStatus.IN_USE
                && b.getStartTime().isBefore(now)
                && b.getEndTime().isAfter(now))
            .map(b -> b.getStationId())
            .collect(Collectors.toList());
    }
}