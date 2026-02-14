package com.example.aws_final.products.dto.request;

import com.example.aws_final.products.model.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequestDto {

    @NotNull(message = "El product name es obligatorio")
    private String name;

    @NotNull(message = "El product type no puede estar vacio")
    private ProductType productType;

    private String description;

    @NotNull(message = "E precio debe ser mayor a cero")
    private BigDecimal precio;

    @NotNull(message = "El numero debe ser mayor a cero")
    private int stock;

    private String imageUrl;
}
