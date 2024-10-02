package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.LocationDetailsDto;
import com.c1se22.publiclaundsmartsystem.payload.LocationSummaryDto;
import com.c1se22.publiclaundsmartsystem.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@AllArgsConstructor
public class LocationController {
    LocationService locationService;
    @GetMapping
    public ResponseEntity<List<LocationSummaryDto>> getAllLocations(){
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDetailsDto> getLocationById(@PathVariable Integer id){
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @PostMapping
    public ResponseEntity<LocationSummaryDto> addLocation(@RequestBody LocationSummaryDto locationSummaryDto){
        return ResponseEntity.ok(locationService.addLocation(locationSummaryDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationSummaryDto> updateLocation(@PathVariable Integer id,
                                                             @RequestBody LocationSummaryDto locationSummaryDto){
        return ResponseEntity.ok(locationService.updateLocation(id, locationSummaryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Integer id){
        locationService.deleteLocation(id);
        return ResponseEntity.ok("Delete location successfully!");
    }
}
