package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.ReservationDto;
import com.c1se22.publiclaundsmartsystem.payload.ReservationResponseDto;
import com.c1se22.publiclaundsmartsystem.service.ReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {
    ReservationService reservationService;
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations(){
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Integer id){
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByUserId(@PathVariable Integer id){
        return ResponseEntity.ok(reservationService.getReservationsByUserId(id));
    }

    @GetMapping("/machine/{id}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByMachineId(@PathVariable Integer id){
        return ResponseEntity.ok(reservationService.getReservationsByMachineId(id));
    }

    @GetMapping("/pending/users")
    public ResponseEntity<ReservationResponseDto> getPendingReservationByUserId(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(reservationService.getPendingReservationByUserId(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody @Valid ReservationDto reservationDto){
        return ResponseEntity.ok(reservationService.createReservation(reservationDto));
    }

    @PutMapping("/complete")
    public ResponseEntity<ReservationResponseDto> completeReservation(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(reservationService.completeReservation(userDetails.getUsername()));
    }

    @PutMapping("/cancel")
    public ResponseEntity<Boolean> cancelReservation(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        reservationService.cancelReservation(userDetails.getUsername());
        return ResponseEntity.ok(true);
    }
    @GetMapping("/period")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsForPeriod(@RequestParam LocalDate start, @RequestParam LocalDate end){
        return ResponseEntity.ok(reservationService.getReservationsForPeriod(start, end));
    }
}
