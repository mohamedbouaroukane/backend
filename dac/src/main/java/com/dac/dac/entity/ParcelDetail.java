package com.dac.dac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Dimensions dimensions;
    private Double price;

}
