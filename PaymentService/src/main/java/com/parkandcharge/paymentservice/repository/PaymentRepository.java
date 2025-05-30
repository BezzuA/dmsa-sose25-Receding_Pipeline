package com.parkandcharge.paymentservice.repository;
import com.parkandcharge.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    List<Payment> findByBookingId(Long bookingId);
}
