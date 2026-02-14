package com.example.aws_final.products.dto.response;

import com.example.aws_final.products.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Integer id;
    private String productName;
    private String description;
    private ProductType type;
    private int stock;
    private BigDecimal price;
    private String imageUrl;
}
