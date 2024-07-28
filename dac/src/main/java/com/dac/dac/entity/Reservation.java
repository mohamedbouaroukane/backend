package com.dac.dac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdIn;
    private LocalDateTime expiredIn;

    private Boolean isUsed =false ;
    private Boolean isExpired =false;
    private String reservationCode;
    private byte[] QRCode;

    @ManyToOne
    @JoinColumn(name="parcel_id")
    private LocalParcel parcel;

    @ManyToOne
    @JoinColumn(name="locker_id")
    private Locker locker;

}
