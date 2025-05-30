package com.parkandcharge.chargingservice.service;
import com.parkandcharge.chargingservice.model.Charging;
import com.parkandcharge.chargingservice.repository.ChargingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChargingStationService {
    private final ChargingStationRepository stationRepo;

    public ChargingStationService(ChargingStationRepository stationRepo) {
        this.stationRepo = stationRepo;
    }

    public List<ChargingStation> getAllStations() {
        return stationRepo.findAll();
    }

    public Optional<ChargingStation> getStation(Long id) {
        return stationRepo.findById(id);
    }

    public ChargingStation createStation(ChargingStation station) {
        return stationRepo.save(station);
    }

    public void deleteStation(Long id) {
        stationRepo.deleteById(id);
    }

    public List<ChargingStation> getStationsByOwner(Long ownerId) {
        return stationRepo.findByOwnerId(ownerId);
    }

    public ChargingStation setInUse(Long id, boolean inUse) {
        ChargingStation station = stationRepo.findById(id).orElseThrow();
        station.setInUse(inUse);
        return stationRepo.save(station);
    }
}
