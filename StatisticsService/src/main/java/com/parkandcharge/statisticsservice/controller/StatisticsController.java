package com.parkandcharge.statisticsservice.controller;
import com.parkandcharge.statisticsservice.model.Statistics;
import com.parkandcharge.statisticsservice.service.StatisticsService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<Statistics> getAllStats() {
        return statisticsService.getAllStats();
    }

    @GetMapping("/owner/{ownerId}")
    public Statistics getStatsByOwner(@PathVariable Long ownerId) {
        return statisticsService.getStatsByOwner(ownerId);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateStatistics(@RequestBody UpdateStatisticsRequest request) {
        statisticsService.updateStatistics(request.getOwnerId(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    public static class UpdateStatisticsRequest {
        private Long ownerId;
        private double amount;
        public Long getOwnerId() { return ownerId; }
        public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }
}
