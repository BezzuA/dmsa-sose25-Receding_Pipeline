package com.parkandcharge.chargingservice.controller;
import com.parkandcharge.chargingservice.model.Charging;
import com.parkandcharge.chargingservice.service.ChargingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin
public class ChargingController {
    private final ChargingService stationService;

    public ChargingController(ChargingService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<Charging> getAllStations() {
        return stationService.getAllStations();
    }

    @GetMapping("/{id}")
    public Optional<Charging> getStation(@PathVariable Long id) {
        return stationService.getStation(id);
    }

    @PostMapping
    public Charging createStation(@RequestBody Charging station) {
        return stationService.createStation(station);
    }

    @DeleteMapping("/{id}")
    public void deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<Charging> getStationsByOwner(@PathVariable Long ownerId) {
        return stationService.getStationsByOwner(ownerId);
    }

    @PutMapping("/{id}/status")
    public Charging setInUse(@PathVariable Long id, @RequestParam boolean inUse) {
        return stationService.setInUse(id, inUse);
    }
}
