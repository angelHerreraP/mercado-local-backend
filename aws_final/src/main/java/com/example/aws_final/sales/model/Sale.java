package com.example.aws_final.sales.model;

import com.example.aws_final.products.model.Product;
import com.example.aws_final.users.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "sales")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;


    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "sale_date", insertable = false, updatable = false)
    private Instant saleDate = Instant.now();// a instant le vale madre la zona horaria (2026-02-04T18:35:21.123Z) -> la Z es la Zona en la que sta


}
