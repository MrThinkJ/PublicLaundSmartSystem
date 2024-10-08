package com.c1se22.publiclaundsmartsystem.entity;

import com.c1se22.publiclaundsmartsystem.enums.MachineStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "machines")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    private Integer id;
    @Column(name = "machine_name", nullable = false)
    private String name;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MachineStatus status;
    @Column(name = "last_maintenance_date", nullable = false)
    private LocalDate lastMaintenanceDate;
    @Column(name = "installation_date", nullable = false)
    private LocalDate installationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User user;
}
