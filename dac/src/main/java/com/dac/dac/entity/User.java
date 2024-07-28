package com.dac.dac.entity;

import com.dac.dac.constants.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String fullName;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false, unique=true)
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean enabled = false;
    private  Boolean locked = false;

    public User(String fullName, String email, String phone, String password, UserRole userRole) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userRole = userRole;
        this.enabled = false;
        this.locked = false;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
