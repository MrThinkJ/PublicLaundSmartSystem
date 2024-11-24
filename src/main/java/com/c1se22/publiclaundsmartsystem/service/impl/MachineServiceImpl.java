package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.UsageHistory;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.payload.internal.FirebaseMachine;
import com.c1se22.publiclaundsmartsystem.entity.Location;
import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.entity.Reservation;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.enums.MachineStatus;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.MachineInUseDto;
import com.c1se22.publiclaundsmartsystem.payload.response.MachineAndTimeDto;
import com.c1se22.publiclaundsmartsystem.payload.request.MachineDto;
import com.c1se22.publiclaundsmartsystem.repository.LocationRepository;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.repository.UsageHistoryRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.service.MachineService;
import com.google.firebase.database.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MachineServiceImpl implements MachineService{
    MachineRepository machineRepository;
    LocationRepository locationRepository;
    UsageHistoryRepository usageHistoryRepository;
    UserRepository userRepository;
    FirebaseDatabase firebaseDatabase;
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
    @Transactional(rollbackFor = Exception.class)
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
        Machine newMachine = machineRepository.save(machine);
        FirebaseMachine firebaseMachine = FirebaseMachine.builder()
                .id(machine.getId())
                .status(String.valueOf(machine.getStatus()))
                .duration(0)
                .build();
        firebaseDatabase.getReference("machines").child(firebaseMachine.getId().toString()).setValueAsync(firebaseMachine);
        log.info("Machine {} has been added", newMachine.getId());
        return mapToDto(newMachine);
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
        firebaseDatabase.getReference("machines").child(id.toString()).removeValueAsync();
        log.info("Machine {} has been deleted", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MachineDto updateMachineStatus(Integer id, String status) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id.toString()));
        MachineStatus machineStatus = MachineStatus.valueOf(status.toUpperCase());
        machine.setStatus(machineStatus);
        Machine updatedMachine = machineRepository.save(machine);
        firebaseDatabase.getReference("machines").child(id.toString()).child("status").setValueAsync(status);
        log.info("Machine {} status has been updated to {}", id, status);
        return mapToDto(updatedMachine);
    }

    @Override
    public MachineDto getMachineAreBeingReservedByUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username));
        return machineRepository.findMachineAreBeingReservedByUser(user.getId()).map(this::mapToDto).orElse(null);
    }

    @Override
    public List<MachineAndTimeDto> getMachinesAreBeingUsedByUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username));
        List<Machine> machines = machineRepository.findMachinesAreBeingUsedByUser(user.getId());
        List<Integer> machineIds = machines.stream().map(Machine::getId).toList();
        List<UsageHistory> usageHistories = usageHistoryRepository.findByCurrentUsedMachineIdsAndUserId(machineIds, user.getId());
        return usageHistories.stream().map(usageHistory -> mapToMachineAndTimeDto(usageHistory.getMachine(), usageHistory)).collect(Collectors.toList());
    }

    @Override
    public List<MachineDto> getMachinesByOwnerId(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id.toString()));
        return machineRepository.findMachinesByOwnerId(user.getId()).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<MachineDto> getMachinesForCurrentOwner(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username));
        return machineRepository.findMachinesByOwnerId(user.getId()).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void updateMachineErrorStatus(Integer id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Machine", "id", id.toString()));
        CompletableFuture<String> future = new CompletableFuture<>();
        String path = "machines/" + id + "/status";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                future.complete(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        String result = future.join();
        if (result != null) {
            MachineStatus machineStatus = MachineStatus.valueOf(result.toUpperCase());
            machine.setStatus(machineStatus);
            machineRepository.save(machine);
        }
        log.info("Machine {} status has been updated to ERROR", id);
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

    private MachineAndTimeDto mapToMachineAndTimeDto(Machine machine, UsageHistory usageHistory) {
        MachineAndTimeDto machineAndTimeDto = MachineAndTimeDto.builder()
                .id(machine.getId())
                .name(machine.getName())
                .model(machine.getModel())
                .capacity(machine.getCapacity())
                .status(String.valueOf(machine.getStatus()))
                .startTime(usageHistory.getStartTime())
                .endTime(usageHistory.getEndTime())
                .build();
        if (machine.getLocation() != null) {
            machineAndTimeDto.setLocationId(machine.getLocation().getId());
            machineAndTimeDto.setLocationName(machine.getLocation().getName());
            machineAndTimeDto.setLocationAddress(machine.getLocation().getAddress());
            machineAndTimeDto.setLocationCity(machine.getLocation().getCity());
            machineAndTimeDto.setLocationDistrict(machine.getLocation().getDistrict());
            machineAndTimeDto.setLocationWard(machine.getLocation().getWard());
            machineAndTimeDto.setLocationLng(machine.getLocation().getLng());
            machineAndTimeDto.setLocationLat(machine.getLocation().getLat());
        }
        return machineAndTimeDto;
    }
}
