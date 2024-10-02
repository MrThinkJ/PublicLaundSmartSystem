package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.LocationDetailsDto;
import com.c1se22.publiclaundsmartsystem.payload.LocationSummaryDto;

import java.util.List;

public interface LocationService {
    List<LocationSummaryDto> getAllLocations();
    LocationDetailsDto getLocationById(Integer id);
    LocationSummaryDto addLocation(LocationSummaryDto locationSummaryDto);
    LocationSummaryDto updateLocation(Integer id, LocationSummaryDto locationSummaryDto);
    void deleteLocation(Integer id);
}
