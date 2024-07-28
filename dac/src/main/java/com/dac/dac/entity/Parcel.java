package com.dac.dac.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;

    @Column(unique = true)
    private String trackingCode;
    @Column(unique = true)
    private String lockerCode;

    private Long pickupCode;
    private Date receiverDate;
    private String parcelDescription;
    private boolean isPrinted;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;



    @ManyToOne
    @JoinColumn(name = "receiver_locker_id")
    private Locker receiverLocker;


    @ManyToOne
    @JoinColumn(name = "receiver_parcel_locker_id")
    private ParcelLocker receiverParcelLocker;

    @ManyToOne
    @JoinColumn(name = "current_status_id")
    private Status status;


    @ManyToOne
    @JoinColumn(name = "parcel_detail_id")
    private ParcelDetail parcelDetail;



    @OneToMany(mappedBy="parcel")
    @OrderBy("date desc ")
    private List<ParcelStatus> statusList;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

}
