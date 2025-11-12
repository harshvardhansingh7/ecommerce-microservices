package com.ecommerce.notification.messaging;

import com.ecommerce.notification.dto.OrderEvent;
import com.ecommerce.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final NotificationService notificationService;

    public OrderEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "order-confirmed")
    public void handleOrderConfirmed(OrderEvent orderEvent) {
        notificationService.sendOrderConfirmation(orderEvent);
    }

    @KafkaListener(topics = "payment-processed")
    public void handlePaymentProcessed(OrderEvent orderEvent) {
        notificationService.sendPaymentConfirmation(orderEvent);
    }

    @KafkaListener(topics = "order-shipped")
    public void handleOrderShipped(OrderEvent orderEvent) {
        notificationService.sendShippingUpdate(orderEvent);
    }
}