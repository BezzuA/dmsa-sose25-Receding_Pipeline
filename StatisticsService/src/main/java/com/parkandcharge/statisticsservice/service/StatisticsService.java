package com.parkandcharge.statisticsservice.service;
import com.parkandcharge.statisticsservice.model.Statistics;
import com.parkandcharge.statisticsservice.repository.StatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${charging.service.url:http://localhost:8082/api/charging}")
    private String chargingServiceUrl;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public List<Statistics> getAllStats() {
        return statisticsRepository.findAll();
    }

    public Statistics getStatsByOwner(Long ownerId) {
        return statisticsRepository.findById(ownerId).orElse(null);
    }

    private List<Long> fetchStationIdsByOwner(Long ownerId) {
        String url = chargingServiceUrl + "/owner/" + ownerId;
        try {
            ChargingStationDto[] stations = restTemplate.getForObject(url, ChargingStationDto[].class);
            if (stations == null) return Collections.emptyList();
            List<Long> ids = new ArrayList<>();
            for (ChargingStationDto station : stations) {
                ids.add(station.getId());
            }
            return ids;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static class ChargingStationDto {
        private Long id;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }

    public void updateStatistics(Long ownerId, double amount) {
        Statistics stats = statisticsRepository.findById(ownerId).orElse(null);
        if (stats == null) {
            stats = new Statistics();
            stats.setOwnerId(ownerId);
            stats.setTotalBookings(1);
            stats.setTotalEarnings(amount);
        } else {
            stats.setTotalBookings(stats.getTotalBookings() + 1);
            stats.setTotalEarnings(stats.getTotalEarnings() + amount);
        }
        statisticsRepository.save(stats);
    }
}
