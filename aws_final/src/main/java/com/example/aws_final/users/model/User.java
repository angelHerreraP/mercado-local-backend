package com.example.aws_final.users.model;


import com.example.aws_final.roles.model.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor // Necesario, para que te puedas comunicar al dedazo con la DB
public class User implements UserDetails { /// El UserDetails es para decirle a Java que este es tu Login

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash",nullable = false)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER) // digamos que pega el valor,no lo uses con queries gigantes
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now(); // a instant le vale madre la zona horaria (2026-02-04T18:35:21.123Z) -> la Z es la Zona en la que sta


    //Empty constructor its already created
    public User(String username, String email, String passwordHash, Role role){
        this.username =username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí convertimos tu objeto Role en algo que Spring entienda
        // Usamos el nombre del rol (ej: "USER" o "ADMIN")
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

