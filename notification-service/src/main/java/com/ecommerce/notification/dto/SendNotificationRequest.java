package com.ecommerce.notification.dto;

import com.ecommerce.notification.model.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendNotificationRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Notification.NotificationType type;

    @NotBlank
    private String recipient;

    @NotBlank
    private String subject;

    private String message;
}