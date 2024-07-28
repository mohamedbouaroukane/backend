package com.dac.dac.service;

import com.dac.dac.entity.Parcel;
import com.dac.dac.entity.ParcelStatus;
import com.dac.dac.entity.Status;
import com.dac.dac.repository.ParcelStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ParcelStatusService {

    @Autowired
    private ParcelStatusRepository parcelStatusRepository;

    public ParcelStatus logParcelStatus(Parcel parcel, Status status) {
        return parcelStatusRepository.save(ParcelStatus.builder()
                .parcel(parcel)
                .status(status)
                .date(new Date())
                .build());
    }
}
