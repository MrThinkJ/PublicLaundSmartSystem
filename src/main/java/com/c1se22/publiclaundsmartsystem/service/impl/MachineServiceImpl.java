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
import lombok.NoArgsConstructor;
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
                new ResourceNotFoundException("Machine", "id", id.toString()));
        return mapToDto(machine);
    }

    @Override
    public MachineDto addMachine(MachineDto machineDto) {
        Location location;
        if (machineDto.getLocationId() != null){
            location = locationRepository.findById(machineDto.getLocationId()).orElseThrow(() ->
                    new ResourceNotFoundException("Location", "id", machineDto.getLocationId().toString()));
        } else{
            location = Location.builder()
                    .name(machineDto.getLocationName())
                    .address(machineDto.getLocationAddress())
                    .city(machineDto.getLocationCity())
                    .district(machineDto.getLocationDistrict())
                    .ward(machineDto.getLocationWard())
                    .lng(0.0)
                    .lat(0.0)
                    .build();
            location = locationRepository.save(location);
        }
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
                new ResourceNotFoundException("Machine", "id", id.toString()));
        Location location = locationRepository.findById(machineDto.getLocationId()).orElseThrow(() ->
                new ResourceNotFoundException("Location", "id", machineDto.getLocationId().toString()));
        machine.setName(machineDto.getName());
        machine.setModel(machineDto.getModel());
        machine.setCapacity(machineDto.getCapacity());
        machine.setLocation(location);
        return mapToDto(machineRepository.save(machine));
    }

    @Override
    public void deleteMachine(Integer id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id.toString()));
        machineRepository.delete(machine);
    }

    @Override
    public MachineDto updateMachineStatus(Integer id, String status) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id.toString()));
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
            machineDto.setLocationCity(machine.getLocation().getCity());
            machineDto.setLocationDistrict(machine.getLocation().getDistrict());
            machineDto.setLocationWard(machine.getLocation().getWard());
            machineDto.setLocationLng(machine.getLocation().getLng());
            machineDto.setLocationLat(machine.getLocation().getLat());
        }
        return machineDto;
    }
}
