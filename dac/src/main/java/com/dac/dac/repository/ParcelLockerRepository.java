package com.dac.dac.repository;

import com.dac.dac.entity.Client;
import com.dac.dac.entity.ParcelLocker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParcelLockerRepository extends JpaRepository<ParcelLocker,Integer> {


    List<ParcelLocker> findByManagerId(int manager_id);
    Optional<ParcelLocker> findByAddressLatitudeAndAddressLongitude(Double latitude,Double longitude);
    Optional<ParcelLocker> findByAddressZipCode(int address_zipCode);
}
