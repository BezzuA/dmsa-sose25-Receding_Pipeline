package com.parkandcharge.chargingservice.service;
import com.parkandcharge.chargingservice.model.Charging;
import com.parkandcharge.chargingservice.repository.ChargingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChargingService {
    private final ChargingRepository stationRepo;

    public ChargingService(ChargingRepository stationRepository) {
        this.stationRepo = stationRepository;
    }

    public List<Charging> getAllStations() {
        return stationRepo.findAll();
    }

    public Optional<Charging> getStation(Long id) {
        return stationRepo.findById(id);
    }

    public Charging createStation(Charging station) {
        return stationRepo.save(station);
    }

    public void deleteStation(Long id) {
        stationRepo.deleteById(id);
    }

    public List<Charging> getStationsByOwner(Long ownerId) {
        return stationRepo.findByOwnerId(ownerId);
    }

    public Charging setInUse(Long id, boolean inUse) {
        Charging station = stationRepo.findById(id).orElseThrow();
        station.setInUse(inUse);
        return stationRepo.save(station);
    }
}
