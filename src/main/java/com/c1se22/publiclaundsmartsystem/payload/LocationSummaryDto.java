package com.c1se22.publiclaundsmartsystem.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationSummaryDto {
    private Integer id;
    private String name;
    private String address;
    private Integer machineCount;
    private List<Integer> machineIds;
}
