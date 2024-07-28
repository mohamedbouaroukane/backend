package com.dac.dac.repository;

import com.dac.dac.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
