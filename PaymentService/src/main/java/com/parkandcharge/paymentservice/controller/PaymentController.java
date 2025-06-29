package com.parkandcharge.paymentservice.controller;
import com.parkandcharge.paymentservice.model.Payment;
import com.parkandcharge.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Optional<Payment> getPayment(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @PostMapping
    public Payment makePayment(@RequestBody Payment payment) {
        return paymentService.makePayment(payment);
    }

    @GetMapping("/user/{userId}")
    public List<Payment> getPaymentsByUser(@PathVariable Long userId) {
        return paymentService.getPaymentsByUser(userId);
    }

    @GetMapping("/booking/{bookingId}")
    public List<Payment> getPaymentsByBooking(@PathVariable Long bookingId) {
        return paymentService.getPaymentsByBooking(bookingId);
    }

    @PostMapping("/booking/{bookingId}/cancel")
    public ResponseEntity<Void> cancelPaymentByBooking(@PathVariable Long bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBooking(bookingId);
        for (Payment payment : payments) {
            payment.setStatus("CANCELLED");
            paymentService.makePayment(payment); // reuses makePayment to save
        }
        return ResponseEntity.ok().build();
    }
}
