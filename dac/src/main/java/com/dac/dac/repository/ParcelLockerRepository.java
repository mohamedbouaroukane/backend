package com.dac.dac.repository;

import com.dac.dac.entity.Client;
import com.dac.dac.entity.ParcelLocker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParcelLockerRepository extends JpaRepository<ParcelLocker,Integer> {


    Optional<ParcelLocker> findByAddressLatitudeAndAddressLongitude(Double latitude,Double longitude);
}
