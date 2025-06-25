package com.parkandcharge.chargingservice.repository;

import com.parkandcharge.chargingservice.model.Charging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChargingRepository extends JpaRepository<Charging, Long> {
    List<Charging> findByOwnerId(Long ownerId);
}
