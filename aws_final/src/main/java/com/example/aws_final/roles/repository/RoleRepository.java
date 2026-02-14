package com.example.aws_final.roles.repository;

import com.example.aws_final.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Este bruh ya se encarga de la implementacion de SQL Queryes en vez de que tu lo escribas
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);


}
/*
En automatico el extender de JPA hace esto:
roleRepository.findAll();
roleRepository.findById(1L);
roleRepository.save(role);
roleRepository.deleteById(2L);

* */