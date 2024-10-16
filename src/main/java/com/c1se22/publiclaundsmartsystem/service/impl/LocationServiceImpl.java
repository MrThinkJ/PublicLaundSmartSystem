package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Location;
import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.LocationDetailsDto;
import com.c1se22.publiclaundsmartsystem.payload.LocationSummaryDto;
import com.c1se22.publiclaundsmartsystem.payload.MachineDto;
import com.c1se22.publiclaundsmartsystem.repository.LocationRepository;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    LocationRepository locationRepository;
    MachineRepository machineRepository;
    @Override
    public List<LocationSummaryDto> getAllLocations() {
        return locationRepository.findAll().stream().map(this::mapToLocationSummaryDto).toList();
    }

    @Override
    public LocationDetailsDto getLocationById(Integer id) {
        Location location = locationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Location", "id", id)
        );
        return mapToLocationDetailsDto(location);
    }

    @Override
    public LocationSummaryDto addLocation(LocationSummaryDto locationSummaryDto) {
        Location location = new Location();
        location.setName(locationSummaryDto.getName());
        location.setAddress(locationSummaryDto.getAddress());
        location.setMachines(Set.of());
        return mapToLocationSummaryDto(locationRepository.save(location));
    }

    @Override
    public LocationSummaryDto updateLocation(Integer id, LocationSummaryDto locationSummaryDto) {
        Location location = locationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Location", "id", id)
        );
        location.setName(locationSummaryDto.getName());
        location.setAddress(locationSummaryDto.getAddress());
        return mapToLocationSummaryDto(locationRepository.save(location));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLocation(Integer id) {
        Location location = locationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Location", "id", id)
        );
        int rowEffect = machineRepository.updateLocationOfMachines(null,
                List.copyOf(location.getMachines().stream().map(Machine::getId).toList()));
        if (rowEffect != location.getMachines().size())
            throw new RuntimeException("Failed to delete location");
        locationRepository.delete(location);
    }

    private LocationSummaryDto mapToLocationSummaryDto(Location location) {
        return LocationSummaryDto.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .machineCount(location.getMachines().size())
                .machineIds(location.getMachines().stream().map(Machine::getId).toList())
                .build();
    }

    private LocationDetailsDto mapToLocationDetailsDto(Location location) {
        return LocationDetailsDto.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .machineCount(location.getMachines().size())
                .machines(location.getMachines().stream().map(this::mapToMachineDto).toList())
                .build();
    }

    private MachineDto mapToMachineDto(Machine machine) {
        return MachineDto.builder()
                .id(machine.getId())
                .name(machine.getName())
                .model(machine.getModel())
                .capacity(machine.getCapacity())
                .status(String.valueOf(machine.getStatus()))
                .locationName(machine.getLocation().getName())
                .locationAddress(machine.getLocation().getAddress())
                .build();
    }
}
