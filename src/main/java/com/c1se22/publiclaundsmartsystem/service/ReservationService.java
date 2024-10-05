package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.ReservationDto;
import com.c1se22.publiclaundsmartsystem.payload.ReservationResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<ReservationResponseDto> getAllReservations();
    ReservationResponseDto getReservationById(Integer reservationId);
    List<ReservationResponseDto> getReservationsByUserId(Integer userId);
    List<ReservationResponseDto> getReservationsByMachineId(Integer machineId);
    ReservationResponseDto createReservation(ReservationDto reservationDto);
    ReservationResponseDto updateReservation(Integer reservationId, ReservationDto reservationDto);
    ReservationResponseDto completeReservation(Integer reservationId);
    List<ReservationResponseDto> getReservationsForPeriod(LocalDate start, LocalDate end);
    void cancelReservation(Integer reservationId);
    void deleteReservation(Integer reservationId);
    int getTotalReservationsForPeriod(LocalDate start, LocalDate end);
    int getTotalReservationsForPeriodByMachineId(LocalDateTime start, LocalDateTime end, Integer machineId);
    int getTotalReservationsForPeriodByUserId(LocalDateTime start, LocalDateTime end, Integer userId);
    int getTotalReservationsForPeriodByWashingTypeId(LocalDateTime start, LocalDateTime end, Integer washingTypeId);
}
