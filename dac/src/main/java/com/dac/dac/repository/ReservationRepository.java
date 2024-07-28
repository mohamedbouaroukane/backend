package com.dac.dac.repository;

import com.dac.dac.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    List<Reservation> findAllByParcelSenderId(int senderId);

    List<Reservation> findAllByOrderByCreatedInDesc();

}
