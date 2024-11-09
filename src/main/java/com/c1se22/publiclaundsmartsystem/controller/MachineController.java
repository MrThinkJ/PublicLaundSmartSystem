package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.MachineAndTimeDto;
import com.c1se22.publiclaundsmartsystem.payload.MachineDto;
import com.c1se22.publiclaundsmartsystem.service.MachineService;
import com.c1se22.publiclaundsmartsystem.service.impl.MachineServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.c1se22.publiclaundsmartsystem.util.AppConstants.*;

@RestController
@RequestMapping("/api/machines")
@AllArgsConstructor
public class MachineController {
    MachineService machineService;

    @GetMapping
    public ResponseEntity<List<MachineDto>> getMachines(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
                                                        @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir) {
        return ResponseEntity.ok(machineService.getMachines(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineDto> getMachineById(@PathVariable Integer id) {
        return ResponseEntity.ok(machineService.getMachineById(id));
    }

    @PostMapping
    public ResponseEntity<MachineDto> addMachine(@RequestBody @Valid MachineDto machineDto) {
        return ResponseEntity.ok(machineService.addMachine(machineDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineDto> updateMachine(@PathVariable Integer id, @RequestBody @Valid MachineDto machineDto) {
        return ResponseEntity.ok(machineService.updateMachine(id, machineDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMachine(@PathVariable Integer id) {
        machineService.deleteMachine(id);
        return ResponseEntity.ok("Machine deleted successfully!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MachineDto> updateMachineStatus(@PathVariable Integer id, @RequestParam String status) {
        return ResponseEntity.ok(machineService.updateMachineStatus(id, status));
    }

    @GetMapping("/user")
    public ResponseEntity<List<MachineAndTimeDto>> getMachinesAreBeingUsedByUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(machineService.getMachinesAreBeingUsedByUser(userDetails.getUsername()));
    }

    @GetMapping("/user/pending")
    public ResponseEntity<MachineDto> getPendingMachinesByUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(machineService.getMachineAreBeingReservedByUser(userDetails.getUsername()));
    }
    @PatchMapping("/error/{id}")
    public ResponseEntity<Boolean> updateMachineErrorStatus(@PathVariable Integer id) {
        machineService.updateMachineErrorStatus(id);
        return ResponseEntity.ok(true);
    }
}
