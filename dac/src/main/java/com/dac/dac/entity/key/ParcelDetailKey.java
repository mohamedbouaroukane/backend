package com.dac.dac.entity.key;

import com.dac.dac.constants.DimensionsKey;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDetailKey implements Serializable {
    @Column(name = "dimensions_id")
    private DimensionsKey dimensionsId;
}
