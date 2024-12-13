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
        String taskId = "complete-" + event.getUsageHistory().getUsageId();
        log.info("Processing washing complete event for usage ID: {}", 
            event.getUsageHistory().getUsageId());
        try {
            Runnable task = () -> {
                log.info("Executing completion task for usage ID: {}", 
                    event.getUsageHistory().getUsageId());
                usageHistoryService.completeUsageHistory(event.getUsageHistory().getUsageId());
                notificationService.sendNotification(event.getUsageHistory().getUser().getId(),
                        String.format("Máy %s đã giặt xong. Hãy lấy quần áo của bạn.",
                                event.getUsageHistory().getMachine().getName()));
            };
            scheduler.schedule(task, 
                Instant.now().plus(event.getRemainingTime(), TimeUnit.MINUTES.toChronoUnit()));
            log.info("Successfully scheduled complete event for usage ID: {} with task ID: {}", 
                event.getUsageHistory().getUsageId(), taskId);
        } catch (Exception e) {
            log.error("Error processing washing complete event: {}", e.getMessage(), e);
            throw e;
        }
    }
}
