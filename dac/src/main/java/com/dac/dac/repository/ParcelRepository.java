package com.dac.dac.repository;

import com.dac.dac.entity.Parcel;
import com.dac.dac.payload.response.ParcelStaticsResponseDto;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel,Integer> {

    public List<Parcel> findAllByReceiverPhone(String phone);




    public Optional<Parcel> findByTrackingCode(String trackingCode);
    public Optional<Parcel> findByLockerCode(String lockerCode);
    public Optional<Parcel> findByPickupCode(Long pickupCode);

    public List<Parcel> findAllByTrackingCodeIn(List<String> trackingCodes);

    public boolean existsByTrackingCode(String traceCode);
    public boolean existsByPickupCode(long pickupCode);


    @Query("SELECT new com.dac.dac.payload.response.ParcelStaticsResponseDto(" +
            "COALESCE(i.creationDate, l.creationDate), " +
            "COALESCE(i.count, 0), " +
            "COALESCE(l.count, 0)) " +
            "FROM " +
            "(SELECT DATE(p.createdAt) as creationDate, COUNT(p) as count " +
            "FROM Parcel p WHERE TYPE(p) = InternationalParcel " +
            "GROUP BY DATE(p.createdAt)) i " +
            "RIGHT JOIN " +
            "(SELECT DATE(p.createdAt) as creationDate, COUNT(p) as count " +
            "FROM Parcel p WHERE TYPE(p) = LocalParcel " +
            "GROUP BY DATE(p.createdAt)) l " +
            "ON i.creationDate = l.creationDate " +
            "ORDER BY COALESCE(i.creationDate, l.creationDate)")
    List<ParcelStaticsResponseDto> countParcelsByTypeStatusAndDate();
}
