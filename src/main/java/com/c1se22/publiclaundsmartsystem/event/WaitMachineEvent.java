package com.c1se22.publiclaundsmartsystem.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitMachineEvent {
    private Integer machineId;
    private Integer duration;
}
