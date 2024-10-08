package com.c1se22.publiclaundsmartsystem.repository;

import com.c1se22.publiclaundsmartsystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>{
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByMachineId(Integer machineId);
    List<Reservation> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
