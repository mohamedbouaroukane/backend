package com.dac.dac.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalParcel extends Parcel{

    private boolean isPrinted;

    @ManyToOne
    @JoinColumn(name = "sender_locker_id")
    private Locker senderLocker;


    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;


    private Double taxAmount;

    private LocalDateTime taxFilingDate;



}
