package com.parkandcharge.statisticsservice.repository;

import com.parkandcharge.statisticsservice.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
} 