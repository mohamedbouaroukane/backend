package com.dac.dac.repository;

import com.dac.dac.constants.DimensionsKey;
import com.dac.dac.entity.Dimensions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimensionsRepository extends JpaRepository<Dimensions, DimensionsKey> {
}
