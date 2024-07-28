package com.dac.dac.repository;

import com.dac.dac.entity.Client;
import com.dac.dac.entity.Courier;
import com.dac.dac.entity.CourierParcelLocker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierParcelLockerRepository extends JpaRepository<CourierParcelLocker,Integer> {

    Optional<CourierParcelLocker> findByAccessCode(String accessCode);
    Optional<CourierParcelLocker> findFirstByParcelLockerIdOrderByCreateDateDesc(int parcelLockerId);
}
