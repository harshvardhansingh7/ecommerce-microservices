package com.ecommerce.notification.service;

import com.ecommerce.notification.dto.OrderEvent;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    public String generateOrderConfirmationEmail(OrderEvent orderEvent) {
        return String.format("""
            Dear Customer,
            
            Thank you for your order! Your order has been confirmed.
            
            Order Details:
            - Order Number: %s
            - Total Amount: $%s
            - Status: %s
            
            We will notify you when your order ships.
            
            Thank you for shopping with us!
            
            Best regards,
            E-commerce Team
            """, orderEvent.getOrderNumber(), orderEvent.getTotalAmount(), orderEvent.getStatus());
    }

    public String generatePaymentConfirmationEmail(OrderEvent orderEvent) {
        return String.format("""
            Dear Customer,
            
            Your payment for order #%s has been successfully processed.
            
            Payment Details:
            - Order Number: %s
            - Amount: $%s
            - Status: Paid
            
            Your order is now being processed and will be shipped soon.
            
            Thank you for your purchase!
            
            Best regards,
            E-commerce Team
            """, orderEvent.getOrderNumber(), orderEvent.getOrderNumber(), orderEvent.getTotalAmount());
    }

    public String generateShippingUpdateEmail(OrderEvent orderEvent) {
        return String.format("""
            Dear Customer,
            
            Your order #%s has been updated.
            
            Current Status: %s
            
            We'll let you know when your order is out for delivery.
            
            Thank you for your patience!
            
            Best regards,
            E-commerce Team
            """, orderEvent.getOrderNumber(), orderEvent.getStatus());
    }
}