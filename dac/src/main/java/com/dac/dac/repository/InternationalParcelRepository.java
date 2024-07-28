package com.dac.dac.repository;

import com.dac.dac.entity.InternationalParcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InternationalParcelRepository extends JpaRepository<InternationalParcel, Integer> {

    @Query("SELECT DATE(p.createdAt) as creationDate, COUNT(p) as count " +
            "FROM InternationalParcel p "+
            "GROUP BY  DATE(p.createdAt)")
    List<Object[]> countParcelsByTypeStatusAndDate();

}
