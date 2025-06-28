package com.parkandcharge.chargingservice.controller;

import com.parkandcharge.chargingservice.model.Charging;
import com.parkandcharge.chargingservice.service.ChargingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charging")
@CrossOrigin
public class ChargingController {

    @Autowired
    private ChargingService chargingService;

    @GetMapping
    public ResponseEntity<List<Charging>> getAllStations() {
        return ResponseEntity.ok(chargingService.getAllStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Charging> getStation(@PathVariable Long id) {
        return chargingService.getStation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Charging> create(@RequestBody Charging charging) {
        return ResponseEntity.ok(chargingService.createStation(charging));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Charging> update(@PathVariable Long id, @RequestBody Charging updated) {
        return ResponseEntity.ok(chargingService.updateStation(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chargingService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Charging>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(chargingService.getStationsByOwner(ownerId));
    }
}
