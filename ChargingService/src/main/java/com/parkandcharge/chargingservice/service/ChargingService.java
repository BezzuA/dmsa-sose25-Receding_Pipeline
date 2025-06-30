package com.parkandcharge.chargingservice.service;

import com.parkandcharge.chargingservice.model.Charging;
import com.parkandcharge.chargingservice.repository.ChargingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Configuration
class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Service
public class ChargingService {

    private final ChargingRepository stationRepo;
    private final RestTemplate restTemplate;
    @Value("${booking.service.url:http://localhost:8082}")
    private String bookingServiceUrl;

    public ChargingService(ChargingRepository stationRepository, RestTemplate restTemplate) {
        this.stationRepo = stationRepository;
        this.restTemplate = restTemplate;
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

    public Charging updateStation(Long id, Charging updated) {
        Charging existing = stationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));

        existing.setName(updated.getName());
        existing.setOwnerId(updated.getOwnerId());
        existing.setPricePerMinute(updated.getPricePerMinute());
        existing.setLatitude(updated.getLatitude());
        existing.setLongitude(updated.getLongitude());
        existing.setAvailableFrom(updated.getAvailableFrom());
        existing.setAvailableUntil(updated.getAvailableUntil());

        return stationRepo.save(existing);
    }

    public void deleteStation(Long id) {
        stationRepo.deleteById(id);
    }

    public List<Charging> getStationsByOwner(Long ownerId) {
        return stationRepo.findByOwnerId(ownerId);
    }

    public List<Long> getChargingIdsByOwner(Long ownerId) {
        return stationRepo.findByOwnerId(ownerId)
                .stream()
                .map(Charging::getId)
                .toList();
    }

    public List<Charging> getAvailableStations() {
        // Get all stations
        List<Charging> allStations = stationRepo.findAll();
        // Get IDs of stations currently in use from BookingService
        String url = bookingServiceUrl + "/api/bookings/in-use-station-ids";
        Long[] inUseIds = restTemplate.getForObject(url, Long[].class);
        List<Long> inUseList = inUseIds != null ? List.of(inUseIds) : List.of();
        // Return only stations not in use
        return allStations.stream()
                .filter(station -> !inUseList.contains(station.getId()))
                .collect(Collectors.toList());
    }
}
