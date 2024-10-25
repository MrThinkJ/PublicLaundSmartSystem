package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.MaintenanceLogsDto;
import com.c1se22.publiclaundsmartsystem.service.MaintenanceLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenancelogs")
@AllArgsConstructor

public class MaintenanceLogController {
    MaintenanceLogService maintenanceLogService;

    @GetMapping
    public ResponseEntity<List<MaintenanceLogsDto>> getMaintenanceLogs(){
        return ResponseEntity.ok(maintenanceLogService.getAllMaintenance());
    }

    @GetMapping ("/{id}")
    public  ResponseEntity<MaintenanceLogsDto> getMaintenanceLogById(@PathVariable Integer id){
        return ResponseEntity.ok(maintenanceLogService.getMaintenanceById(id));
    }

    @GetMapping ("/machine/{machineId}")
    public  ResponseEntity<List<MaintenanceLogsDto>> getMaintenanceLogbyMachineId(@PathVariable Integer machineId){
        return ResponseEntity.ok(maintenanceLogService.getAllMaintenanceByMachineId(machineId));
    }

    @PostMapping
    public ResponseEntity<MaintenanceLogsDto> addMaintenanceLog(@RequestBody MaintenanceLogsDto maintenanceLogsDto){
        return ResponseEntity.ok(maintenanceLogService.addMaintenance(maintenanceLogsDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceLogsDto> updateMaintenanceLog(@PathVariable Integer id, @RequestBody MaintenanceLogsDto maintenanceLogsDto){
        return ResponseEntity.ok(maintenanceLogService.updateMaintenance(id, maintenanceLogsDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMaintenanceLog(@PathVariable Integer id){
        maintenanceLogService.deleteMaintenance(id);
        return ResponseEntity.ok("MaintenanceLog deleted successfully");
    }
}
