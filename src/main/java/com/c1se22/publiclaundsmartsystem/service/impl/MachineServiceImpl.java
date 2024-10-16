package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Location;
import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.enums.MachineStatus;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.MachineDto;
import com.c1se22.publiclaundsmartsystem.repository.LocationRepository;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.service.MachineService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MachineServiceImpl implements MachineService{
    MachineRepository machineRepository;
    LocationRepository locationRepository;

    @Override
    public List<MachineDto> getAllMachines() {
        return machineRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<MachineDto> getMachines(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase(
                Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return machineRepository.findAll(pageable).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public MachineDto getMachineById(Integer id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id));
        return mapToDto(machine);
    }

    @Override
    public MachineDto addMachine(MachineDto machineDto) {
        Location location = locationRepository.findById(machineDto.getLocationId()).orElseThrow(() ->
                new ResourceNotFoundException("Location", "id", machineDto.getLocationId()));
        Machine machine = Machine.builder()
                .name(machineDto.getName())
                .model(machineDto.getModel())
                .capacity(machineDto.getCapacity())
                .status(MachineStatus.AVAILABLE)
                .location(location)
                .lastMaintenanceDate(LocalDate.now())
                .installationDate(LocalDate.now())
                .build();
        return mapToDto(machineRepository.save(machine));
    }

    @Override
    public MachineDto updateMachine(Integer id, MachineDto machineDto) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id));
        Location location = locationRepository.findById(machineDto.getLocationId()).orElseThrow(() ->
                new ResourceNotFoundException("Location", "id", machineDto.getLocationId()));
        machine.setName(machineDto.getName());
        machine.setModel(machineDto.getModel());
        machine.setCapacity(machineDto.getCapacity());
        machine.setLocation(location);
        return mapToDto(machineRepository.save(machine));
    }

    @Override
    public void deleteMachine(Integer id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id));
        machineRepository.delete(machine);
    }

    @Override
    public MachineDto updateMachineStatus(Integer id, String status) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id));
        MachineStatus machineStatus = MachineStatus.valueOf(status.toUpperCase());
        machine.setStatus(machineStatus);
        return mapToDto(machineRepository.save(machine));
    }

    @Override
    public List<MachineDto> getMachinesAreBeingUsedByUser(Integer userId) {
        return machineRepository.findMachinesAreBeingUsedByUser(userId).stream().map(this::mapToDto).collect(Collectors.toList());
    }


    private MachineDto mapToDto(Machine machine) {
        MachineDto machineDto = MachineDto.builder()
                .id(machine.getId())
                .name(machine.getName())
                .model(machine.getModel())
                .capacity(machine.getCapacity())
                .status(String.valueOf(machine.getStatus()))
                .build();
        if (machine.getLocation() != null) {
            machineDto.setLocationId(machine.getLocation().getId());
            machineDto.setLocationName(machine.getLocation().getName());
            machineDto.setLocationAddress(machine.getLocation().getAddress());
        }
        return machineDto;
    }
}
