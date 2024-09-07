package com.dac.dac.repository;

import com.dac.dac.entity.LocalParcel;
import com.dac.dac.entity.Locker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LocalParcelRepository extends JpaRepository<LocalParcel,Integer> {


}
