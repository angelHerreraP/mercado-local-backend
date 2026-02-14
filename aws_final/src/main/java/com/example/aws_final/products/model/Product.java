package com.example.aws_final.products.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class) // Escucha cuando se crea/edita
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Audited // <--- Si alguien cambia el stock, Hibernate Envers lo anota
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type", nullable = false)
    private ProductType type;

    @Column(name = "descripcion") // <--- Así mapeas el nombre real de la DB
    private String descripcion;

    @PositiveOrZero(message = "El precio debe ser positivo")
    @Column(nullable = false)
    private BigDecimal precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private int stock;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    private Instant updatedAt = Instant.now();

    public Product(String name, ProductType type, String descripcion, BigDecimal price, int stock) {
        this.name = name;
        this.type = type;
        this.descripcion = descripcion;
        this.precio = price;
        this.stock = stock;
    }
}
