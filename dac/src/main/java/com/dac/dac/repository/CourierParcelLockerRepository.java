package com.dac.dac.repository;

import com.dac.dac.entity.Client;
import com.dac.dac.entity.Courier;
import com.dac.dac.entity.CourierParcelLocker;
import com.dac.dac.payload.response.CourierAccessResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CourierParcelLockerRepository extends JpaRepository<CourierParcelLocker,Integer> {

    Optional<CourierParcelLocker> findByAccessCode(String accessCode);
    Optional<CourierParcelLocker> findFirstByParcelLockerIdOrderByCreateDateDesc(int parcelLockerId);
    @Query("SELECT new com.dac.dac.payload.response.CourierAccessResponseDto(cpl.courier.fullName, cpl.parcelLocker.name, cpl.accessDate) FROM CourierParcelLocker cpl ORDER BY cpl.createDate DESC")
    List<CourierAccessResponseDto> findFirstTenRecords(Pageable pageable);

    @Query("SELECT DISTINCT c FROM Courier c " +
            "LEFT JOIN CourierParcelLocker cpl ON c = cpl.courier " +
            "WHERE c.id NOT IN (" +
            "    SELECT cpl2.courier.id FROM CourierParcelLocker cpl2 " +
            "    WHERE cpl2.accessDate >= :cutoffDate " +
            "    GROUP BY cpl2.courier.id " +
            "    HAVING MAX(cpl2.accessDate) >= :cutoffDate" +
            ") OR NOT EXISTS (SELECT 1 FROM CourierParcelLocker cpl3 WHERE cpl3.courier = c)")
    List<Courier> findCouriersNotAccessedSince(@Param("cutoffDate") Date cutoffDate);
}
