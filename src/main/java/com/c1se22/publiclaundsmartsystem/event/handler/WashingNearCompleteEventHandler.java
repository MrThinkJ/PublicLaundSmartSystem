package com.c1se22.publiclaundsmartsystem.event.handler;

import com.c1se22.publiclaundsmartsystem.entity.UsageHistory;
import com.c1se22.publiclaundsmartsystem.event.WashingCompleteEvent;
import com.c1se22.publiclaundsmartsystem.event.WashingNearCompleteEvent;
import com.c1se22.publiclaundsmartsystem.service.EventService;
import com.c1se22.publiclaundsmartsystem.service.NotificationService;
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
public class WashingNearCompleteEventHandler {
    TaskScheduler scheduler;
    EventService eventService;
    NotificationService notificationService;

    @Async
    @EventListener
    public void handleWashingNearCompleteEvent(WashingNearCompleteEvent event) {
        UsageHistory usageHistory = event.getUsageHistory();
        Runnable task = () -> {
            eventService.publishEvent(new WashingCompleteEvent(usageHistory));
            notificationService.sendNotification(usageHistory.getUser().getId(),
                    String.format("Your washing in machine %s will complete in %s minutes",
                            usageHistory.getMachine().getName(), AppConstants.TIME_TO_NOTIFY_USER));
        };
        scheduler.schedule(task,
                Instant.now().plus(event.getDuration()- AppConstants.TIME_TO_NOTIFY_USER,
                        TimeUnit.MINUTES.toChronoUnit()));
    }
}
