package com.ecommerce.notification.service;

import com.ecommerce.notification.dto.NotificationDTO;
import com.ecommerce.notification.dto.OrderEvent;
import com.ecommerce.notification.dto.SendNotificationRequest;
import com.ecommerce.notification.model.Notification;
import com.ecommerce.notification.repository.NotificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;

    public NotificationService(NotificationRepository notificationRepository,
                               JavaMailSender mailSender,
                               EmailTemplateService emailTemplateService) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
        this.emailTemplateService = emailTemplateService;
    }

    public NotificationDTO sendNotification(SendNotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType().name());
        notification.setRecipient(request.getRecipient());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());

        try {
            if (request.getType() == Notification.NotificationType.EMAIL) {
                sendEmail(notification);
            } else if (request.getType() == Notification.NotificationType.SMS) {
                sendSms(notification);
            }

            notification.setStatus(Notification.NotificationStatus.SENT);
            notification.setSentAt(java.time.LocalDateTime.now());
        } catch (Exception e) {
            notification.setStatus(Notification.NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
        }

        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    public void sendOrderConfirmation(OrderEvent orderEvent) {
        String subject = "Order Confirmation - #" + orderEvent.getOrderNumber();
        String message = emailTemplateService.generateOrderConfirmationEmail(orderEvent);

        SendNotificationRequest request = new SendNotificationRequest();
        request.setUserId(orderEvent.getUserId());
        request.setType(Notification.NotificationType.EMAIL);
        request.setRecipient(orderEvent.getUserEmail());
        request.setSubject(subject);
        request.setMessage(message);

        sendNotification(request);
    }

    public void sendPaymentConfirmation(OrderEvent orderEvent) {
        String subject = "Payment Confirmation - #" + orderEvent.getOrderNumber();
        String message = emailTemplateService.generatePaymentConfirmationEmail(orderEvent);

        SendNotificationRequest request = new SendNotificationRequest();
        request.setUserId(orderEvent.getUserId());
        request.setType(Notification.NotificationType.EMAIL);
        request.setRecipient(orderEvent.getUserEmail());
        request.setSubject(subject);
        request.setMessage(message);

        sendNotification(request);
    }

    public void sendShippingUpdate(OrderEvent orderEvent) {
        String subject = "Shipping Update - Order #" + orderEvent.getOrderNumber();
        String message = emailTemplateService.generateShippingUpdateEmail(orderEvent);

        SendNotificationRequest request = new SendNotificationRequest();
        request.setUserId(orderEvent.getUserId());
        request.setType(Notification.NotificationType.EMAIL);
        request.setRecipient(orderEvent.getUserEmail());
        request.setSubject(subject);
        request.setMessage(message);

        sendNotification(request);
    }

    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getPendingNotifications() {
        return notificationRepository.findByStatus(Notification.NotificationStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void retryFailedNotifications() {
        List<Notification> failedNotifications = notificationRepository
                .findByStatus(Notification.NotificationStatus.FAILED);

        for (Notification notification : failedNotifications) {
            if (notification.getRetryCount() < 3) {
                try {
                    if (notification.getType().equals("EMAIL")) {
                        sendEmail(notification);
                    } else if (notification.getType().equals("SMS")) {
                        sendSms(notification);
                    }

                    notification.setStatus(Notification.NotificationStatus.SENT);
                    notification.setSentAt(java.time.LocalDateTime.now());
                } catch (Exception e) {
                    notification.setRetryCount(notification.getRetryCount() + 1);
                    notification.setErrorMessage(e.getMessage());
                }
                notificationRepository.save(notification);
            }
        }
    }

    private void sendEmail(Notification notification) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(notification.getRecipient());
        mailMessage.setSubject(notification.getSubject());
        mailMessage.setText(notification.getMessage());

        mailSender.send(mailMessage);
    }

    private void sendSms(Notification notification) {
        // Implement SMS sending logic
        // This would integrate with services like Twilio, AWS SNS, etc.
        System.out.println("SMS sent to: " + notification.getRecipient());
        System.out.println("Message: " + notification.getMessage());
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setType(notification.getType());
        dto.setRecipient(notification.getRecipient());
        dto.setSubject(notification.getSubject());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setSentAt(notification.getSentAt());
        return dto;
    }
}