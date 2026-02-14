package com.example.aws_final.products.repository;

import com.example.aws_final.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String name);

    // Removed invalid findByProductId
    List<Product> findByNameContainingIgnoreCase(String name); // barra de busqueda

    List<Product> findProductByTypeName(String typeName);

    List<Product> findByStockLessThan(Integer limit);
}
