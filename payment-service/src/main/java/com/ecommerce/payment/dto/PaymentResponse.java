package com.ecommerce.payment.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private boolean success;
    private String message;
    private String transactionId;
    private PaymentDTO payment;
}