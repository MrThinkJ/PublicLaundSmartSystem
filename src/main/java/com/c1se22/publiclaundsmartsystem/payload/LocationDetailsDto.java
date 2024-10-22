package com.c1se22.publiclaundsmartsystem.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDetailsDto {
    private Integer id;
    private String name;
    private String address;
    private Integer machineCount;
    private Double lat;
    private Double lng;
    private List<MachineDto> machines;
}
