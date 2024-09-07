package com.dac.dac.repository;

import com.dac.dac.entity.ParcelStatus;
import com.dac.dac.payload.response.ParcelStatusStaticsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParcelStatusRepository extends JpaRepository<ParcelStatus,Integer> {

    List<ParcelStatus> findAllByParcelTrackingCodeOrderByDateDesc(String traceCode);

}
