package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.model.Payment;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentGatewayService {

    private final Random random = new Random();

    public boolean processPayment(PaymentRequest request) {
        // Simulate payment gateway processing
        // In real implementation, integrate with actual payment gateway like Stripe, PayPal, etc.
        
        // Simulate random success/failure (85% success rate for demo)
        boolean success = random.nextDouble() < 0.85;
        
        // Simulate processing delay
        try {
            Thread.sleep(1000 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return success;
    }

    public boolean processRefund(Payment payment) {
        // Simulate refund processing
        boolean success = random.nextDouble() < 0.90;
        
        try {
            Thread.sleep(1500 + random.nextInt(2500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return success;
    }
}