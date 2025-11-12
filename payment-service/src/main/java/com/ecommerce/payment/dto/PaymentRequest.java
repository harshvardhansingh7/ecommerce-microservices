package com.ecommerce.payment.dto;

import com.ecommerce.payment.model.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull
    private Long orderId;

    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Payment.PaymentMethod method;

    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String cardHolderName;
}