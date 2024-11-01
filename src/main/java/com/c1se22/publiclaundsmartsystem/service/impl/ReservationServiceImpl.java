package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.*;
import com.c1se22.publiclaundsmartsystem.enums.ReservationStatus;
import com.c1se22.publiclaundsmartsystem.exception.InsufficientBalanceException;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.MachineInUseDto;
import com.c1se22.publiclaundsmartsystem.payload.ReservationDto;
import com.c1se22.publiclaundsmartsystem.payload.ReservationResponseDto;
import com.c1se22.publiclaundsmartsystem.payload.UsageHistoryDto;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.repository.ReservationRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.repository.WashingTypeRepository;
import com.c1se22.publiclaundsmartsystem.service.MachineService;
import com.c1se22.publiclaundsmartsystem.service.ReservationService;
import com.c1se22.publiclaundsmartsystem.service.UsageHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    ReservationRepository reservationRepository;
    UserRepository userRepository;
    MachineRepository machineRepository;
    WashingTypeRepository washingTypeRepository;
    MachineService machineService;
    UsageHistoryService usageHistoryService;

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
        if (!machine.getStatus().name().equals("AVAILABLE")) {
            return null;
        }
        WashingType washingType = washingTypeRepository.findById(reservationDto.getWashingTypeId()).orElseThrow(
                () -> new ResourceNotFoundException("WashingType", "washingTypeId", reservationDto.getWashingTypeId())
        );
        // Check user balance
        BigDecimal userBalance = user.getBalance();
        BigDecimal washingTypeCost = washingType.getDefaultPrice();
        
        if (userBalance.compareTo(washingTypeCost) < 0) {
            throw new InsufficientBalanceException("User does not have sufficient balance for this washing type.");
        }
        Reservation reservation = Reservation.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(15))
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

        // Save the updated reservation
        Reservation savedReservation = reservationRepository.save(reservation);
        
        UsageHistoryDto usageHistoryDTO = UsageHistoryDto.builder()
                .userId(savedReservation.getUser().getId())
                .machineId(savedReservation.getMachine().getId())
                .washingTypeId(savedReservation.getWashingType().getId())
                .cost(savedReservation.getWashingType().getDefaultPrice())
                .build();
        usageHistoryService.createUsageHistory(usageHistoryDTO);
        // Update user balance
        User user = savedReservation.getUser();
        user.setBalance(user.getBalance().subtract(savedReservation.getWashingType().getDefaultPrice()));
        userRepository.save(user);
        return mapToResponseDto(savedReservation);
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
        Machine machine = reservation.getMachine();
        machineService.updateMachineStatus(machine.getId(), "AVAILABLE");
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

    @Override
    public List<MachineInUseDto> findMachineInUse() {
        return reservationRepository.findMachineInUse().stream().map(this::mapToInUseDto).collect(Collectors.toList());
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

    private MachineInUseDto mapToInUseDto( Reservation reservation){
        return MachineInUseDto.builder()
                .machineId(reservation.getMachine().getId())
                .machineName(reservation.getMachine().getName())
                .status(String.valueOf(reservation.getMachine().getStatus()))
                .locationName(reservation.getMachine().getLocation().getName())
                .address(reservation.getMachine().getLocation() != null ? reservation.getMachine().getLocation().getAddress() : "Null")
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .userName(reservation.getUser().getUsername())
                .userId(reservation.getUser().getId())
                .build();
    }
}
