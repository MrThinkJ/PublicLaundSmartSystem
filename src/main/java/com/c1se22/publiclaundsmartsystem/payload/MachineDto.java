package com.c1se22.publiclaundsmartsystem.payload;

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
    private String locationCity;
    private String locationDistrict;
    private String locationWard;
    private Double locationLng;
    private Double locationLat;
}
