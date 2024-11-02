package com.c1se22.publiclaundsmartsystem.payload;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDto {
    private Integer reservationId;
    @NotNull(message = "User id is required")
    private Integer userId;
    @NotNull(message = "Machine id is required")
    private Integer machineId;
    @NotNull(message = "Washing type id is required")
    private Integer washingTypeId;
}
