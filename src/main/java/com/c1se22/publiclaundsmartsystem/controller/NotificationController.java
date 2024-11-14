package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.NotificationDto;
import com.c1se22.publiclaundsmartsystem.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/notifications")
@AllArgsConstructor
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Integer id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<NotificationDto>> getNotifications(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userDetails.getUsername()));
    }

    @GetMapping("/user/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUser(userDetails.getUsername()));
    }

    @GetMapping("/user/read")
    public ResponseEntity<List<NotificationDto>> getReadNotifications(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getReadNotificationsByUser(userDetails.getUsername()));
    }

    @PatchMapping("/user/mark/{id}")
    public ResponseEntity<Boolean> markNotificationAsRead(@PathVariable Integer id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok(true);
    }

    @PatchMapping("/user/mark/all")
    public ResponseEntity<Boolean> markAllNotificationsAsRead(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        notificationService.markAllNotificationsAsRead(userDetails.getUsername());
        return ResponseEntity.ok(true);
    }
}
