package com.c1se22.publiclaundsmartsystem.repository;

import com.c1se22.publiclaundsmartsystem.entity.MaintenanceLog;
import com.c1se22.publiclaundsmartsystem.payload.MaintenanceLogsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaintenanceLogRespository extends JpaRepository<MaintenanceLog, Integer> {
    List<MaintenanceLog> findAllMaintenanceByMachineId(Integer machineId);
}
