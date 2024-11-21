package com.c1se22.publiclaundsmartsystem.event.handler;

import com.c1se22.publiclaundsmartsystem.event.WashingCompleteEvent;
import com.c1se22.publiclaundsmartsystem.service.NotificationService;
import com.c1se22.publiclaundsmartsystem.service.UsageHistoryService;
import com.c1se22.publiclaundsmartsystem.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class WashingCompleteEventHandler {
    TaskScheduler scheduler;
    UsageHistoryService usageHistoryService;
    NotificationService notificationService;

    @Async
    @EventListener
    public void handleWashingCompleteEvent(WashingCompleteEvent event) {
        log.info("Processing washing complete event for usage ID: {}", 
            event.getUsageHistory().getUsageId());
        try {
            Runnable task = () -> {
                usageHistoryService.completeUsageHistory(event.getUsageHistory().getUsageId());
                notificationService.sendNotification(event.getUsageHistory().getUser().getId(),
                        String.format("Your washing session in machine %s has been completed. Please collect your clothes.",
                                event.getUsageHistory().getMachine().getName()));
            };
            scheduler.schedule(task, Instant.now().plus(AppConstants.TIME_TO_NOTIFY_USER, TimeUnit.MINUTES.toChronoUnit()));
            log.info("Successfully scheduled completion notification for usage ID: {}", 
                event.getUsageHistory().getUsageId());
        } catch (Exception e) {
            log.error("Error processing washing complete event: {}", e.getMessage(), e);
            throw e;
        }
    }
}
