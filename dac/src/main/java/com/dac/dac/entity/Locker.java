package com.dac.dac.entity;

import com.dac.dac.constants.LockerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;
    private int pin;

    @Enumerated(EnumType.STRING)
    private LockerStatus status;

    @ManyToOne
    @JoinColumn(name = "parcel_locker_id")
    @JsonIgnore
    private ParcelLocker parcelLocker;

    @ManyToOne
    @JoinColumn(name = "locker_size_id")
    private Dimensions lockerSize;

    @OneToOne
    @JsonIgnore
    private Parcel currentParcel;


}
