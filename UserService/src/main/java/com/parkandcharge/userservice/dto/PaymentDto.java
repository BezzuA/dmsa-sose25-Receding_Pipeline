package com.parkandcharge.userservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Long id;
    private Long userId;
    private Double amount;
    private String status;
    private LocalDateTime paymentTime;
}
