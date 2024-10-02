package com.c1se22.publiclaundsmartsystem.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Integer id;
    private String name;
    private String address;
    private Integer machineCount;
    private List<Integer> machines;
}
