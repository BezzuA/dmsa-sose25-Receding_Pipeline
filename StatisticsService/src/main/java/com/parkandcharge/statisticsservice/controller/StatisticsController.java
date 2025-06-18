package com.parkandcharge.statisticsservice.controller;
import com.parkandcharge.statisticsservice.model.Statistics;
import com.parkandcharge.statisticsservice.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/station/{stationId}")
    public Statistics getStationStats(@PathVariable Long stationId) {
        return statisticsService.getStationStats(stationId);
    }

    @GetMapping
    public List<Statistics> getAllStats() {
        return statisticsService.getAllStats();
    }
}
