package com.dac.dac.repository;

import com.dac.dac.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Integer> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
