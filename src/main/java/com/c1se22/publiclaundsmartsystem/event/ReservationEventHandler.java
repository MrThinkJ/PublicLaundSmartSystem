package com.c1se22.publiclaundsmartsystem.event;

import com.c1se22.publiclaundsmartsystem.entity.Reservation;
import com.c1se22.publiclaundsmartsystem.enums.ReservationStatus;
import com.c1se22.publiclaundsmartsystem.repository.ReservationRepository;
import com.c1se22.publiclaundsmartsystem.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class ReservationEventHandler {
    ReservationService reservationService;
    ReservationRepository reservationRepository;
    @Async
    @EventListener
    public void handleReservationEvent(ReservationCreatedEvent event){
        Reservation reservation = event.getReservation();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() ->{
            Reservation currentReservation = reservationRepository.findById(reservation.getId()).orElse(null);
            if (currentReservation != null && currentReservation.getStatus().equals(ReservationStatus.PENDING)){
                reservationService.cancelReservation(currentReservation.getId());
            }
        }, 15, TimeUnit.MINUTES);
        scheduler.shutdown();
    }
}
