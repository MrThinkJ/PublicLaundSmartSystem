package com.c1se22.publiclaundsmartsystem.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDto {
    private Integer reservationId;
    private Integer userId;
    private Integer machineId;
    private Integer washingTypeId;
}
