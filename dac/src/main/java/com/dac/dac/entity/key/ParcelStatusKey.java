package com.dac.dac.entity.key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Embeddable
@Data
@AllArgsConstructor
public class ParcelStatusKey implements Serializable {
    private int parcelId;
    private int statusId;
    private Date date;
}
