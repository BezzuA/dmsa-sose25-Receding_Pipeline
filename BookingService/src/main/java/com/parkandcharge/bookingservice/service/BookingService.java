package com.parkandcharge.bookingservice.service;
import com.parkandcharge.bookingservice.dto.BookingRequestDto;
import com.parkandcharge.bookingservice.dto.ChargingDto;
import com.parkandcharge.bookingservice.model.Booking;
import com.parkandcharge.bookingservice.model.BookingStatus;
import com.parkandcharge.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        // Here, you’d check station/user availability, but for mockup, just save
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void cancelBooking(Long id) {
        bookingRepository.findById(id).ifPresent(booking -> {
            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new IllegalStateException("Only pending bookings can be cancelled.");
            }

//            // Refund the user
//            String url = "http://userservice/api/users/" + booking.getUserId() + "/add?amount=" + booking.getAmount();
//            restTemplate.postForObject(url, null, Void.class);

            // Set status to CANCELLED
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
        });
    }

    public Booking completeBooking(Long id) {
        Booking booking = getBooking(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Get the station info from ChargingService
        ChargingDto station = restTemplate.getForObject(
                "http://chargingservice/stations/" + booking.getStationId(), ChargingDto.class);

        if (station == null) {
            throw new RuntimeException("Station not found");
        }

        // Credit the owner's balance
        String topUpUrl = "http://userservice/api/users/" + station.getOwnerId() + "/topup?amount=" + booking.getAmount();
        restTemplate.postForObject(topUpUrl, null, Void.class);

        // Update booking status
        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }

    public List<ChargingDto> getAvailableStations(LocalDateTime start, LocalDateTime end) {
        List<Long> bookedIds = bookingRepository.findConflictingStationIds(start, end);

        ChargingDto[] stations = restTemplate.getForObject("http://chargingservice/stations", ChargingDto[].class);

        return Arrays.stream(stations)
                .filter(station -> !bookedIds.contains(station.getId()))
                .collect(Collectors.toList());
    }

    public Booking bookStation(BookingRequestDto request) {
        // Validate availability
        List<Long> conflictingIds = bookingRepository.findConflictingStationIds(request.getStartTime(), request.getEndTime());
        if (conflictingIds.contains(request.getStationId())) {
            throw new IllegalStateException("Station is not available at this time");
        }

        String url = "http://userservice/api/users/" + request.getUserId() + "/deduct?amount=" + request.getAmount();
        restTemplate.postForObject(url, null, Void.class); // if 200 OK, it’s deducted

        // Save booking
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setStationId(request.getStationId());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setAmount(request.getAmount());
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

}