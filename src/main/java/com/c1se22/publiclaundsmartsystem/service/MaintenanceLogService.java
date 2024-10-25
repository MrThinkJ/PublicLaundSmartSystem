package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.MaintenanceLogsDto;

import java.util.List;

public interface MaintenanceLogService {
    List<MaintenanceLogsDto> getAllMaintenance();
    List<MaintenanceLogsDto> getAllMaintenanceByMachineId(Integer id);
    MaintenanceLogsDto getMaintenanceById(Integer id);
    MaintenanceLogsDto addMaintenance(MaintenanceLogsDto maintenanceLogsDto);
    MaintenanceLogsDto updateMaintenance(Integer id, MaintenanceLogsDto maintenanceLogsDto);
    void deleteMaintenance(Integer id);
}
