package com.c1se22.publiclaundsmartsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer id;
    @Column(name = "maintenance_type", nullable = false)
    private String maintenanceType;
    @Column(name = "description", nullable = false)
    private String maintenanceDescription;
    @Column(name = "maintenance_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal maintenanceCost;
    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;
    @Column(name = "technician_name", nullable = false)
    private String technicianName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
