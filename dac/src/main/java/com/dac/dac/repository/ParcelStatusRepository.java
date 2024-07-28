package com.dac.dac.repository;

import com.dac.dac.entity.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelStatusRepository extends JpaRepository<ParcelStatus,Integer> {

    List<ParcelStatus> findAllByParcelTrackingCodeOrderByDateDesc(String traceCode);
}
