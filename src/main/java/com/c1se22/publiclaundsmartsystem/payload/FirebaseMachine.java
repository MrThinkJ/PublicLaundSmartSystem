package com.c1se22.publiclaundsmartsystem.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseMachine {
    private Integer id;
    private String status;
}
