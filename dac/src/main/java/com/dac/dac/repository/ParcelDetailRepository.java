package com.dac.dac.repository;

import com.dac.dac.constants.DimensionsKey;
import com.dac.dac.entity.Dimensions;
import com.dac.dac.entity.Parcel;
import com.dac.dac.entity.ParcelDetail;
import com.dac.dac.entity.key.ParcelDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParcelDetailRepository extends JpaRepository<ParcelDetail, DimensionsKey> {
    public Optional<ParcelDetail> findByDimensionsId( DimensionsKey id);
}
