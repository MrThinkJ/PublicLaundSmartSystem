package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.entity.Reservation;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.entity.WashingType;
import com.c1se22.publiclaundsmartsystem.enums.ReservationStatus;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.ReservationDto;
import com.c1se22.publiclaundsmartsystem.payload.ReservationResponseDto;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.repository.ReservationRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.repository.WashingTypeRepository;
import com.c1se22.publiclaundsmartsystem.service.MachineService;
import com.c1se22.publiclaundsmartsystem.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    ReservationRepository reservationRepository;
    UserRepository userRepository;
    MachineRepository machineRepository;
    WashingTypeRepository washingTypeRepository;
    MachineService machineService;

    @Override
    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public ReservationResponseDto getReservationById(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResourceNotFoundException("Reservation", "reservationId", reservationId)
        );
        return mapToResponseDto(reservation);
    }

    @Override
    public List<ReservationResponseDto> getReservationsByUserId(Integer userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        if (reservations != null) {
            return reservations.stream()
                    .map(this::mapToResponseDto)
                    .toList();
        }
        return null;
    }

    @Override
    public List<ReservationResponseDto> getReservationsByMachineId(Integer machineId) {
        List<Reservation> reservations = reservationRepository.findByMachineId(machineId);
        if (reservations != null) {
            return reservations.stream()
                    .map(this::mapToResponseDto)
                    .toList();
        }
        return null;
    }

    @Override
    public ReservationResponseDto createReservation(ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", reservationDto.getUserId())
        );
        Machine machine = machineRepository.findById(reservationDto.getMachineId()).orElseThrow(
                () -> new ResourceNotFoundException("Machine", "machineId", reservationDto.getMachineId())
        );
        WashingType washingType = washingTypeRepository.findById(reservationDto.getWashingTypeId()).orElseThrow(
                () -> new ResourceNotFoundException("WashingType", "washingTypeId", reservationDto.getWashingTypeId())
        );
        Reservation reservation = Reservation.builder()
                .startTime(null)
                .endTime(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .user(user)
                .machine(machine)
                .washingType(washingType)
                .build();
        machineService.updateMachineStatus(machine.getId(), "IN_USE");
        return mapToResponseDto(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponseDto updateReservation(Integer reservationId, ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", reservationDto.getUserId())
        );
        Machine machine = machineRepository.findById(reservationDto.getMachineId()).orElseThrow(
                () -> new ResourceNotFoundException("Machine", "machineId", reservationDto.getMachineId())
        );
        WashingType washingType = washingTypeRepository.findById(reservationDto.getWashingTypeId()).orElseThrow(
                () -> new ResourceNotFoundException("WashingType", "washingTypeId", reservationDto.getWashingTypeId())
        );
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResourceNotFoundException("Reservation", "reservationId", reservationId)
        );
        reservation.setUser(user);
        reservation.setMachine(machine);
        reservation.setWashingType(washingType);
        reservation.setUpdatedAt(LocalDateTime.now());
        return mapToResponseDto(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponseDto completeReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResourceNotFoundException("Reservation", "reservationId", reservationId)
        );
        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusMinutes(reservation.getWashingType().getDefaultDuration()));
        return mapToResponseDto(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationResponseDto> getReservationsForPeriod(LocalDate start, LocalDate end) {
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay();
        List<Reservation> reservations = reservationRepository.findByCreatedAtBetween(startTime, endTime);
        return reservations.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public void cancelReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResourceNotFoundException("Reservation", "reservationId", reservationId)
        );
        reservation.setStatus(ReservationStatus.CANCELED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResourceNotFoundException("Reservation", "reservationId", reservationId)
        );
        reservation.setStatus(ReservationStatus.ARCHIVED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    @Override
    public int getTotalReservationsForPeriod(LocalDate start, LocalDate end) {
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay();
        return 0;
    }

    @Override
    public int getTotalReservationsForPeriodByMachineId(LocalDateTime start, LocalDateTime end, Integer machineId) {
        return 0;
    }

    @Override
    public int getTotalReservationsForPeriodByUserId(LocalDateTime start, LocalDateTime end, Integer userId) {
        return 0;
    }

    @Override
    public int getTotalReservationsForPeriodByWashingTypeId(LocalDateTime start, LocalDateTime end, Integer washingTypeId) {
        return 0;
    }

    private ReservationResponseDto mapToResponseDto(Reservation reservation) {
        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .status(reservation.getStatus().name())
                .userId(reservation.getUser().getId())
                .machineId(reservation.getMachine().getId())
                .washingType(reservation.getWashingType())
                .build();
    }

    private ReservationDto mapToDto(Reservation reservation) {
        return ReservationDto.builder()
                .reservationId(reservation.getReservationId())
                .userId(reservation.getUser().getId())
                .machineId(reservation.getMachine().getId())
                .washingTypeId(reservation.getWashingType().getId())
                .build();
    }
}
