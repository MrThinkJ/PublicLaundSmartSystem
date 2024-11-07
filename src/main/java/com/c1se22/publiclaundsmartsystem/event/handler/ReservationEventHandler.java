package com.c1se22.publiclaundsmartsystem.event.handler;

import com.c1se22.publiclaundsmartsystem.entity.Reservation;
import com.c1se22.publiclaundsmartsystem.enums.ReservationStatus;
import com.c1se22.publiclaundsmartsystem.event.ReservationCreatedEvent;
import com.c1se22.publiclaundsmartsystem.repository.ReservationRepository;
import com.c1se22.publiclaundsmartsystem.service.NotificationService;
import com.c1se22.publiclaundsmartsystem.service.ReservationService;
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
public class ReservationEventHandler {
    ReservationService reservationService;
    NotificationService notificationService;
    ReservationRepository reservationRepository;
    TaskScheduler scheduler;
    @Async
    @EventListener
    public void handleReservationEvent(ReservationCreatedEvent event){
        Reservation reservation = event.getReservation();
        Runnable task = () -> {
            Reservation currentReservation = reservationRepository.findById(reservation.getId()).orElse(null);
            if (currentReservation != null && currentReservation.getStatus().equals(ReservationStatus.PENDING)){
                reservationService.cancelReservation(currentReservation.getUser().getUsername());
                notificationService.sendNotification(currentReservation.getUser().getId(),
                        "Your reservation has been cancelled due to inactivity");
            }
        };
        scheduler.schedule(task,
                Instant.now().plus(AppConstants.TIME_TO_CANCEL_RESERVATION, TimeUnit.MINUTES.toChronoUnit()));
    }
}
