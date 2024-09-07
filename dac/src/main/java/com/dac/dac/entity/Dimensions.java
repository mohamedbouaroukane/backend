package com.dac.dac.entity;

import com.dac.dac.constants.DimensionsKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Dimensions {
    @Id
    @Enumerated(EnumType.STRING)
    private DimensionsKey id;
    private Double width;
    private Double height;
    private Double depth;
    private Double weight;
}
