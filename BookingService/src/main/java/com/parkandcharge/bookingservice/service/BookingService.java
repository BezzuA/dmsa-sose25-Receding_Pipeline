package com.parkandcharge.bookingservice.service;
import com.parkandcharge.bookingservice.dto.BookingRequestDto;
import com.parkandcharge.bookingservice.dto.ChargingDto;
import com.parkandcharge.bookingservice.model.Booking;
import com.parkandcharge.bookingservice.model.BookingStatus;
import com.parkandcharge.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${charging.service.url:http://charging-service}")
    private String chargingServiceUrl;

    @Value("${user.service.url:http://user-service}")
    private String userServiceUrl;

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
        // Here, you'd check station/user availability, but for mockup, just save
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

            // Refund the user
            String refundUrl = userServiceUrl + "/api/users/" + booking.getUserId() + "/add?amount=" + booking.getAmount();
            restTemplate.postForObject(refundUrl, null, Void.class);

            // Set status to CANCELLED
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            // Set payment status to CANCELLED in PaymentService
            String paymentServiceUrl = "http://payment-service/api/payments/booking/" + booking.getId() + "/cancel";
            restTemplate.postForObject(paymentServiceUrl, null, Void.class);
        });
    }

    public Booking completeBooking(Long id) {
        Booking booking = getBooking(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Get the station info from ChargingService
        ChargingDto station = restTemplate.getForObject(
                chargingServiceUrl + "/api/charging/" + booking.getStationId(), ChargingDto.class);

        if (station == null) {
            throw new RuntimeException("Station not found");
        }

        // Deduct from user
        String deductUrl = userServiceUrl + "/api/users/" + booking.getUserId() + "/deduct?amount=" + booking.getAmount();
        restTemplate.postForObject(deductUrl, null, Void.class);

        // Credit the owner's balance
        String topUpUrl = userServiceUrl + "/api/users/" + station.getOwnerId() + "/topup?amount=" + booking.getAmount();
        restTemplate.postForObject(topUpUrl, null, Void.class);

        // Create payment record for owner
        String paymentServiceUrl = "http://payment-service/api/payments";
        java.util.Map<String, Object> ownerPaymentRequest = new java.util.HashMap<>();
        ownerPaymentRequest.put("userId", station.getOwnerId());
        ownerPaymentRequest.put("amount", booking.getAmount());
        ownerPaymentRequest.put("bookingId", booking.getId());
        restTemplate.postForObject(paymentServiceUrl, ownerPaymentRequest, Void.class);

        // Update booking status
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);

        // Create payment record in PaymentService
        java.util.Map<String, Object> paymentRequest = new java.util.HashMap<>();
        paymentRequest.put("userId", booking.getUserId());
        paymentRequest.put("amount", booking.getAmount());
        paymentRequest.put("bookingId", booking.getId());
        restTemplate.postForObject(paymentServiceUrl, paymentRequest, Void.class);

        return booking;
    }

    public List<ChargingDto> getAvailableStations(LocalDateTime start, LocalDateTime end) {
        List<Long> bookedIds = bookingRepository.findConflictingStationIds(start, end);

        ChargingDto[] stations = restTemplate.getForObject(chargingServiceUrl + "/api/charging", ChargingDto[].class);

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

        // Fetch station info for availability check
        ChargingDto station = restTemplate.getForObject(
            chargingServiceUrl + "/api/charging/" + request.getStationId(), ChargingDto.class);
        if (station == null) {
            throw new IllegalStateException("Station not found");
        }
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalStateException("Booking start and end time must be provided");
        }
        if (request.getStartTime().isBefore(station.getAvailableFrom()) ||
            request.getEndTime().isAfter(station.getAvailableUntil())) {
            throw new IllegalStateException("Station not available at this time");
        }

        // Calculate duration in minutes
        long durationMinutes = java.time.Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
        double amount = station.getPricePerMinute() * durationMinutes;

        String url = userServiceUrl + "/api/users/" + request.getUserId() + "/deduct?amount=" + amount;
        restTemplate.postForObject(url, null, Void.class); // if 200 OK, it's deducted

        // Save booking
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setStationId(request.getStationId());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setAmount(amount);
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByStationIds(List<Long> stationIds) {
        return bookingRepository.findByStationIdIn(stationIds);
    }

}