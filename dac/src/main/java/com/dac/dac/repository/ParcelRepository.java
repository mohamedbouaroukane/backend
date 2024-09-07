package com.dac.dac.repository;

import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.Parcel;
import com.dac.dac.entity.Status;
import com.dac.dac.payload.response.CourierPayedParcelResponse;
import com.dac.dac.payload.response.ParcelStaticsResponseDto;
import com.dac.dac.payload.response.ParcelStatusStaticsResponseDto;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel,Integer> {

    public List<Parcel> findAllByReceiverPhone(String phone);


    @Query("SELECT new com.dac.dac.payload.response.CourierPayedParcelResponse(p.trackingCode, ps.date) " +
            "FROM LocalParcel p JOIN ParcelStatus ps ON p.id = ps.parcel.id " +
            "WHERE p.status.statusLabel = :paidStatus " +
            "AND p.senderLocker.parcelLocker.manager.id = :courierId " +
            "AND ps.status.statusLabel = :paidStatus " +
            "AND EXISTS (SELECT 1 FROM ParcelStatus ps2 " +
            "            WHERE ps2.parcel = p " +
            "            AND ps2.status.statusLabel = :paidStatus)")
    List<CourierPayedParcelResponse> findPaidParcelsInLocker(
            @Param("paidStatus") ParcelStatusConst paidStatus,
            @Param("courierId") int courierId);

    public Optional<Parcel> findByTrackingCode(String trackingCode);
    public Optional<Parcel> findByLockerCode(String lockerCode);
    public Optional<Parcel> findByPickupCode(Long pickupCode);

    public List<Parcel> findAllByTrackingCodeIn(List<String> trackingCodes);
    public List<Parcel> findAllByTrackingCodeInAndStatusStatusLabelOrStatusStatusLabel(Collection<String> trackingCode, ParcelStatusConst status_statusLabel, ParcelStatusConst status_statusLabel2);

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

    @Query("select new com.dac.dac.payload.response.ParcelStatusStaticsResponseDto(ps.status.statusLabel,count(*))  from Parcel ps group by (ps.status.statusLabel)")
    List<ParcelStatusStaticsResponseDto> findAllParcelStatus();

}
