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

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class WashingCompleteEventHandler {
    TaskScheduler scheduler;
    UsageHistoryService usageHistoryService;
    NotificationService notificationService;

    @Async
    @EventListener
    public void handleWashingCompleteEvent(WashingCompleteEvent event) {
        Runnable task = () -> {
            usageHistoryService.completeUsageHistory(event.getUsageHistory().getUsageId());
            notificationService.sendNotification(event.getUsageHistory().getUser().getId(),
                    String.format("Your washing session in machine %s has been completed. Please collect your clothes.",
                            event.getUsageHistory().getMachine().getName()));
        };
        scheduler.schedule(task, Instant.now().plus(AppConstants.TIME_TO_NOTIFY_USER, TimeUnit.MINUTES.toChronoUnit()));
    }
}
