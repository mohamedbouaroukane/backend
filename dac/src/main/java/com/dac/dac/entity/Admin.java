package com.dac.dac.entity;

import com.dac.dac.constants.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@RequiredArgsConstructor
@Table(name="client")

public class Admin extends User {

    @Override
    public UserRole getUserRole() {
        return UserRole.ADMIN;
    }

    @Override
    public void setUserRole(UserRole userRole) {
        super.setUserRole(UserRole.ADMIN);
    }
}
