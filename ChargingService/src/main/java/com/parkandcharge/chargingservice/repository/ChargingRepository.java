package com.parkandcharge.chargingservice.repository;
import com.parkandcharge.chargingservice.model.Charging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, Long> {
    List<ChargingStation> findByInUse(boolean inUse);
    List<ChargingStation> findByOwnerId(Long ownerId);
}
