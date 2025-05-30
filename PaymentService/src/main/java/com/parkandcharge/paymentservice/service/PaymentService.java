package com.parkandcharge.paymentservice.service;
import com.parkandcharge.paymentservice.model.Payment;
import com.parkandcharge.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;

    public PaymentService(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    public Optional<Payment> getPayment(Long id) {
        return paymentRepo.findById(id);
    }

    public Payment makePayment(Payment payment) {
        payment.setStatus("PAID"); // For mock
        payment.setPaymentTime(LocalDateTime.now());
        return paymentRepo.save(payment);
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepo.findByUserId(userId);
    }

    public List<Payment> getPaymentsByBooking(Long bookingId) {
        return paymentRepo.findByBookingId(bookingId);
    }
}
