package com.example.aws_final.sales.repository;


import com.example.aws_final.sales.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    //Para listar posteriormente las ventas o buscarlas si se necesita cambair
    // List<Sale> findByProductId(Integer productId);

    List<Sale> findByUserId(Integer userId);


    List<Sale> findBySaleDateBetweenOrderBySaleDateDesc(Instant start, Instant end);

    // 4. TOTAL DINERO POR USUARIO: Para saber quién es tu mejor vendedor
    @Query("SELECT SUM(s.total) FROM Sale s WHERE s.user.id = :userId")
    BigDecimal sumTotalSalesByUser(@Param("userId") Integer userId);

    // 5. TOTAL DINERO DEL PERIODO: Para el corte de caja rápido
    @Query("SELECT SUM(s.total) FROM Sale s WHERE s.saleDate >= :start AND s.saleDate <= :end")
    BigDecimal sumTotalRevenueInPeriod(@Param("start") Instant start, @Param("end") Instant end);
}
    // List prod vendidos hoy x modulo en PDV mama

