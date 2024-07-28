package com.dac.dac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourierParcelLocker {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String accessCode;
    private Date createDate;
    private Date accessDate;
    private Date expiryDate;
    private boolean isUsed=false;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne
    @JoinColumn(name = "parcel_locker_id")
    private ParcelLocker parcelLocker;


}
