package com.c1se22.publiclaundsmartsystem.payload;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MachineDto {
    private Integer id;
    private String name;
    private String model;
    private Integer capacity;
    private String status;
    private Integer locationId;
    private String locationName;
    private String locationAddress;
}
