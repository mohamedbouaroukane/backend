package com.dac.dac.entity;

import com.dac.dac.entity.key.ParcelStatusKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelStatus {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "parcel_id")
    @JsonIgnore
    private Parcel parcel;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @JsonIgnore
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}
