package com.dac.dac.entity.key;

import com.dac.dac.constants.DimensionsKey;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimensionsPrimaryKey implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private DimensionsKey id;
}
