package com.dac.dac.repository;

import com.dac.dac.constants.DimensionsKey;
import com.dac.dac.constants.LockerStatus;
import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LockerRepository extends JpaRepository<Locker,Integer> {


    Optional<Locker> findFirstByStatusAndLockerSizeIdAndParcelLockerId(LockerStatus status, DimensionsKey lockerSize, int parcelLocker_id);

    @Query("SELECT COUNT(l) FROM Locker l WHERE  l.parcelLocker.id = :parcelLockerId")
    long countByParcelLockerId( @Param("parcelLockerId") int parcelLockerId);

    @Query("SELECT COUNT(l) FROM Locker l WHERE l.status = :status AND l.parcelLocker.id = :parcelLockerId")
    long countByStatusAndParcelLockerId(@Param("status") LockerStatus status, @Param("parcelLockerId") int parcelLockerId);

    @Query("SELECT COUNT(l) FROM Locker l WHERE l.currentParcel.status.statusLabel = :status AND l.parcelLocker.id = :parcelLockerId")
    long countByParcelStatusAndParcelLockerId(@Param("status") ParcelStatusConst status, @Param("parcelLockerId") int parcelLockerId);

    @Query("SELECT COUNT(l) FROM Locker l WHERE l.currentParcel.isPrinted = :isPrinted AND l.parcelLocker.id = :parcelLockerId")
    long countByParcelIsPrintedAndParcelLockerId(@Param("isPrinted") boolean isPrinted, @Param("parcelLockerId") int parcelLockerId);

    @Query("SELECT l.currentParcel FROM Locker l WHERE l.status = :status AND l.currentParcel.isPrinted = :printed AND l.parcelLocker.id = :parcelLockerId AND l.currentParcel.status.statusLabel = :parcelStatus ")
    List<Parcel> findAllParcelsByLockerStatusAndPrintedAndParcelLockerIdAndCurrentParcelStatus(@Param("status") LockerStatus status, @Param("printed") boolean printed, @Param("parcelLockerId") int parcelLockerId, @Param("parcelStatus") ParcelStatusConst parcelStatus);



    @Query("SELECT l FROM Locker l WHERE l.status = :status AND l.parcelLocker.id = :parcelLockerId AND l.currentParcel.status.statusLabel = :parcelStatus")
    List<Locker> findAllByStatusAndParcelLockerIdAndParcelStatus(@Param("status") LockerStatus status, @Param("parcelLockerId") int parcelLockerId, @Param("parcelStatus") ParcelStatusConst parcelStatus);

    @Query("SELECT l FROM Locker l WHERE l.status = :status AND l.parcelLocker.id = :parcelLockerId AND l.currentParcel.status.statusLabel = :parcelStatus AND l.currentParcel.receiverDate < current_timestamp()")
    List<Locker> findAllByExpiredDate(@Param("status") LockerStatus status, @Param("parcelLockerId") int parcelLockerId, @Param("parcelStatus") ParcelStatusConst parcelStatus);



}
