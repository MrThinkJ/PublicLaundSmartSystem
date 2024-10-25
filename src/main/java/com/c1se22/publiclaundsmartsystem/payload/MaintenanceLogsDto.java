package com.c1se22.publiclaundsmartsystem.payload;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogsDto {
    private Integer id;
    private String maintenanceType;
    private String maintenanceDescription;
    private BigDecimal maintenanceCost;
    private LocalDate maintenanceDate;
    private LocalDate completionDate;
    private String technicianName;
    private Integer machineId;
}
