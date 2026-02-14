package com.example.aws_final.products.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "product_type")
@Builder
@Audited
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    public ProductType(String name){
        this.name = name;
    }
}
