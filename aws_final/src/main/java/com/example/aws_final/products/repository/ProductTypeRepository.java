package com.example.aws_final.products.repository;

import com.example.aws_final.products.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {

    Optional<ProductType> findByName(String name);
    boolean existsByName(String name);
}
