package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.MachineInUseDto;
import com.c1se22.publiclaundsmartsystem.payload.ReservationDto;
import com.c1se22.publiclaundsmartsystem.payload.ReservationResponseDto;
import com.c1se22.publiclaundsmartsystem.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/machines/inuse")
    public ResponseEntity<List<MachineInUseDto>> getMachineInUse(){
        return ResponseEntity.ok(reservationService.findMachineInUse());
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationDto reservationDto){
        return ResponseEntity.ok(reservationService.createReservation(reservationDto));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteReservation(@PathVariable Integer id){
//        reservationService.deleteReservation(id);
//        return ResponseEntity.ok("Reservation deleted");
//    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ReservationResponseDto> completeReservation(@PathVariable Integer id){
        return ResponseEntity.ok(reservationService.completeReservation(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Boolean> cancelReservation(@PathVariable Integer id){
        reservationService.cancelReservation(id);
        return ResponseEntity.ok(true);
    }
    @GetMapping("/period")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsForPeriod(@RequestParam LocalDate start, @RequestParam LocalDate end){
        return ResponseEntity.ok(reservationService.getReservationsForPeriod(start, end));
    }
}
