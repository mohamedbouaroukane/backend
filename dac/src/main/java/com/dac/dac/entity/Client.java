package com.dac.dac.entity;

import com.dac.dac.constants.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@RequiredArgsConstructor
@Table(name="client")

public class Client extends User {

    @Override
    public UserRole getUserRole() {
        return UserRole.CLIENT;
    }

    @Override
    public void setUserRole(UserRole userRole) {
        super.setUserRole(UserRole.CLIENT);
    }
}
