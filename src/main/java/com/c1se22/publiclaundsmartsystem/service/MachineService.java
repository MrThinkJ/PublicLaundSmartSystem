package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.payload.MachineDto;

import java.awt.print.Pageable;
import java.util.List;

public interface MachineService {
    List<MachineDto> getAllMachines();
    List<MachineDto> getMachines(int page, int size, String sortBy, String sortDir);
    MachineDto getMachineById(Integer id);
    MachineDto addMachine(MachineDto machineDto);
    MachineDto updateMachine(Integer id, MachineDto machineDto);
    void deleteMachine(Integer id);
    MachineDto updateMachineStatus(Integer id, String status);
}
