package com.ecommerce.notification.controller;

import com.ecommerce.notification.dto.NotificationDTO;
import com.ecommerce.notification.dto.SendNotificationRequest;
import com.ecommerce.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification Management", description = "APIs for sending and managing notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    @Operation(summary = "Send notification")
    public ResponseEntity<NotificationDTO> sendNotification(@Valid @RequestBody SendNotificationRequest request) {
        NotificationDTO notification = notificationService.sendNotification(request);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications by user ID")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending notifications")
    public ResponseEntity<List<NotificationDTO>> getPendingNotifications() {
        List<NotificationDTO> notifications = notificationService.getPendingNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/retry-failed")
    @Operation(summary = "Retry failed notifications")
    public ResponseEntity<Void> retryFailedNotifications() {
        notificationService.retryFailedNotifications();
        return ResponseEntity.ok().build();
    }
}