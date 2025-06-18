package com.parkandcharge.statisticsservice.service;
import com.parkandcharge.statisticsservice.model.Statistics;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticsService {

    public Statistics getStationStats(Long stationId) {
        // Mock example data
        Statistics stats = new Statistics();
        stats.setStationId(stationId);
        stats.setTotalBookings(new Random().nextInt(100)); // random bookings
        stats.setTotalEarnings(stats.getTotalBookings() * 5.0); // e.g., $5 per booking
        return stats;
    }

    public List<Statistics> getAllStats() {
        // Mock a few stations
        List<Statistics> statsList = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            statsList.add(getStationStats(i));
        }
        return statsList;
    }
}
