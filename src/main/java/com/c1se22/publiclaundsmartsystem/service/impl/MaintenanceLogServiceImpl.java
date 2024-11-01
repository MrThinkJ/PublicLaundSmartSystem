package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.entity.MaintenanceLog;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.MaintenanceLogsDto;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.repository.MaintenanceLogRepository;
import com.c1se22.publiclaundsmartsystem.service.MaintenanceLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MaintenanceLogServiceImpl implements MaintenanceLogService {
    MaintenanceLogRepository maintenanceLogRepository;
    MachineRepository machineRepository;

    @Override
    public List<MaintenanceLogsDto> getAllMaintenance() {
        return maintenanceLogRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceLogsDto> getAllMaintenanceByMachineId(Integer machineId) {
        return maintenanceLogRepository.findAllMaintenanceByMachineId(machineId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public MaintenanceLogsDto getMaintenanceById(Integer id) {
        MaintenanceLog maintenanceLog = maintenanceLogRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("MaintenanceLog", "id", id)
        );
        return mapToDto(maintenanceLog);
    }

    @Override
    public MaintenanceLogsDto addMaintenance(MaintenanceLogsDto maintenanceLogsDto) {
        MaintenanceLog maintenanceLog = new MaintenanceLog();
        maintenanceLog.setMaintenanceType(maintenanceLogsDto.getMaintenanceType());
        maintenanceLog.setMaintenanceDescription(maintenanceLogsDto.getMaintenanceDescription());
        maintenanceLog.setMaintenanceCost(maintenanceLogsDto.getMaintenanceCost());
        maintenanceLog.setMaintenanceDate(maintenanceLogsDto.getMaintenanceDate());
        maintenanceLog.setCompletionDate(maintenanceLogsDto.getCompletionDate());
        maintenanceLog.setTechnicianName(maintenanceLogsDto.getTechnicianName());

        Machine machine = machineRepository.findById(maintenanceLogsDto.getMachineId())
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", maintenanceLogsDto.getMachineId()));
        maintenanceLog.setMachine(machine);

        return mapToDto(maintenanceLogRepository.save(maintenanceLog));
    }

    @Override
    public MaintenanceLogsDto updateMaintenance(Integer id, MaintenanceLogsDto maintenanceLogsDto) {
        MaintenanceLog maintenanceLog = maintenanceLogRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("MaintenanceLog", "id", id)
        );
        maintenanceLog.setMaintenanceType(maintenanceLogsDto.getMaintenanceType());
        maintenanceLog.setMaintenanceDescription(maintenanceLogsDto.getMaintenanceDescription());
        maintenanceLog.setMaintenanceCost(maintenanceLogsDto.getMaintenanceCost());
        maintenanceLog.setMaintenanceDate(maintenanceLogsDto.getMaintenanceDate());
        maintenanceLog.setCompletionDate(maintenanceLogsDto.getCompletionDate());
        maintenanceLog.setTechnicianName(maintenanceLogsDto.getTechnicianName());
        Machine machine = machineRepository.findById(maintenanceLogsDto.getMachineId())
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", maintenanceLogsDto.getMachineId()));
        maintenanceLog.setMachine(machine);

        return mapToDto(maintenanceLogRepository.save(maintenanceLog));
    }

    @Override
    public void deleteMaintenance(Integer id) {
        MaintenanceLog maintenanceLog = maintenanceLogRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("MaintenanceLog", "id", id)
        );
        maintenanceLogRepository.delete(maintenanceLog);
    }

    public MaintenanceLogsDto mapToDto(MaintenanceLog maintenanceLog){
        return MaintenanceLogsDto.builder()
                .id(maintenanceLog.getId())
                .maintenanceType(maintenanceLog.getMaintenanceType())
                .maintenanceDescription(maintenanceLog.getMaintenanceDescription())
                .maintenanceCost(maintenanceLog.getMaintenanceCost())
                .maintenanceDate(maintenanceLog.getMaintenanceDate())
                .completionDate(maintenanceLog.getCompletionDate())
                .technicianName(maintenanceLog.getTechnicianName())
                .machineId(maintenanceLog.getMachine() != null ? maintenanceLog.getMachine().getId() : null)
                .build();
    }
}
