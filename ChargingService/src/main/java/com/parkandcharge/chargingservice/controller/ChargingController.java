package com.parkandcharge.chargingservice.controller;
import com.parkandcharge.chargingservice.model.Charging;
import com.parkandcharge.chargingservice.service.ChargingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin
public class ChargingStationController {
    private final ChargingStationService stationService;

    public ChargingStationController(ChargingStationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<ChargingStation> getAllStations() {
        return stationService.getAllStations();
    }

    @GetMapping("/{id}")
    public Optional<ChargingStation> getStation(@PathVariable Long id) {
        return stationService.getStation(id);
    }

    @PostMapping
    public ChargingStation createStation(@RequestBody ChargingStation station) {
        return stationService.createStation(station);
    }

    @DeleteMapping("/{id}")
    public void deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<ChargingStation> getStationsByOwner(@PathVariable Long ownerId) {
        return stationService.getStationsByOwner(ownerId);
    }

    @PutMapping("/{id}/status")
    public ChargingStation setInUse(@PathVariable Long id, @RequestParam boolean inUse) {
        return stationService.setInUse(id, inUse);
    }
}
