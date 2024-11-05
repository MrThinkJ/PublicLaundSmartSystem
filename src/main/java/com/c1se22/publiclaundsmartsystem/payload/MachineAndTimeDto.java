package com.c1se22.publiclaundsmartsystem.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MachineAndTimeDto {
    private Integer id;
    @NotBlank(message = "Name is not blank")
    @NotNull(message = "Name is required")
    private String name;
    @NotBlank(message = "Model is not blank")
    @NotNull(message = "Model is required")
    private String model;
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;
    private String status;
    @NotNull(message = "Location Id is required")
    private Integer locationId;
    private String locationName;
    private String locationAddress;
    private String locationCity;
    private String locationDistrict;
    private String locationWard;
    private Double locationLng;
    private Double locationLat;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
