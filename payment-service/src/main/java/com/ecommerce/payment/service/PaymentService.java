package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentDTO;
import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.model.Payment;
import com.ecommerce.payment.repository.PaymentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentGatewayService paymentGatewayService;

    public PaymentService(PaymentRepository paymentRepository,
                          KafkaTemplate<String, Object> kafkaTemplate,
                          PaymentGatewayService paymentGatewayService) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentGatewayService = paymentGatewayService;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        // Create payment record
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());

        Payment savedPayment = paymentRepository.save(payment);

        // Process payment with gateway (simulated)
        boolean paymentSuccess = paymentGatewayService.processPayment(request);

        if (paymentSuccess) {
            savedPayment.setStatus(Payment.PaymentStatus.SUCCESS);
            savedPayment.setPaymentGatewayResponse("Payment processed successfully");
        } else {
            savedPayment.setStatus(Payment.PaymentStatus.FAILED);
            savedPayment.setPaymentGatewayResponse("Payment processing failed");
        }

        Payment updatedPayment = paymentRepository.save(savedPayment);

        // Send payment status event
        sendPaymentStatusEvent(updatedPayment);

        PaymentResponse response = new PaymentResponse();
        response.setSuccess(paymentSuccess);
        response.setTransactionId(updatedPayment.getTransactionId());
        response.setPayment(convertToDTO(updatedPayment));
        response.setMessage(paymentSuccess ? "Payment successful" : "Payment failed");

        return response;
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }

    public List<PaymentDTO> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO updatePaymentStatus(Long paymentId, Payment.PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);

        sendPaymentStatusEvent(updatedPayment);

        return convertToDTO(updatedPayment);
    }

    public PaymentDTO refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new RuntimeException("Only successful payments can be refunded");
        }

        // Simulate refund process
        boolean refundSuccess = paymentGatewayService.processRefund(payment);

        if (refundSuccess) {
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
            payment.setPaymentGatewayResponse("Payment refunded successfully");
        } else {
            throw new RuntimeException("Refund processing failed");
        }

        Payment updatedPayment = paymentRepository.save(payment);
        sendPaymentStatusEvent(updatedPayment);

        return convertToDTO(updatedPayment);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void sendPaymentStatusEvent(Payment payment) {
        // Send event to Kafka for other services (Order, Notification)
        kafkaTemplate.send("payment-processed", convertToDTO(payment));
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setUserId(payment.getUserId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setMethod(payment.getMethod());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}