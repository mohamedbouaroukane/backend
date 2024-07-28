package com.dac.dac.repository;

import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Integer> {

    public Optional<Status> findByStatusLabel(ParcelStatusConst statusLabel);
}
