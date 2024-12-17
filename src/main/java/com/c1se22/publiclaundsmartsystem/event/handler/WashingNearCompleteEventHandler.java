package com.c1se22.publiclaundsmartsystem.event.handler;

import com.c1se22.publiclaundsmartsystem.entity.UsageHistory;
import com.c1se22.publiclaundsmartsystem.event.WashingCompleteEvent;
import com.c1se22.publiclaundsmartsystem.event.WashingNearCompleteEvent;
import com.c1se22.publiclaundsmartsystem.service.EventService;
import com.c1se22.publiclaundsmartsystem.service.NotificationService;
import com.c1se22.publiclaundsmartsystem.util.AppConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class WashingNearCompleteEventHandler {
    TaskScheduler scheduler;
    EventService eventService;
    NotificationService notificationService;

    @Async
    @EventListener
    public void handleWashingNearCompleteEvent(WashingNearCompleteEvent event) {
        UsageHistory usageHistory = event.getUsageHistory();
        String taskId = "near-complete-" + usageHistory.getUsageId();
        log.info("Processing washing near complete event for usage ID: {}", usageHistory.getUsageId());
        Integer timeToNotify = Math.toIntExact(Math.round((event.getDuration() * AppConstants.TIME_TO_NOTIFY_USER)));
        Runnable task = () -> {
            log.info("Executing near completion task for usage ID: {}", usageHistory.getUsageId());
            eventService.publishEvent(new WashingCompleteEvent(usageHistory, event.getDuration() - timeToNotify));
            notificationService.sendNotification(usageHistory.getUser().getId(),
                    String.format("Đồ của bạn ở máy %s sẽ hoàn thành trong %d phút. Vui lòng chuẩn bị!",
                            usageHistory.getMachine().getName(), event.getDuration() - timeToNotify));
        };
        
        scheduler.schedule(task,
                Instant.now().plus(timeToNotify,
                        TimeUnit.MINUTES.toChronoUnit()));
        log.info("Successfully scheduled near complete event for usage ID: {} with task ID: {}", 
            usageHistory.getUsageId(), taskId);
    }
}
