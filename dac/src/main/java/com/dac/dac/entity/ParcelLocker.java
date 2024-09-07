package com.dac.dac.entity;

import com.dac.dac.constants.ParcelLockerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="parcel_locker")
public class ParcelLocker {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

   // private String address;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "zipCode")
    private Address address;

    @Enumerated(EnumType.STRING)
    private ParcelLockerStatus status;

    @OneToMany(mappedBy="parcelLocker",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Locker> lockers;


    @ManyToOne
    @JoinColumn(name = "courier_manager")
    private Courier manager;


}
