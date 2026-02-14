package com.example.aws_final.products.dto.request;

import com.example.aws_final.products.model.ProductType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequestDto {

    @NotBlank(message = "El product name es obligatorio")
    private String name;

    @NotBlank(message = "El product type no puede estar vacio")
    private ProductType productType;

    private String descripcion;

    @NotBlank(message = "E precio debe ser mayor a cero")
    private BigDecimal precio;

    @NotBlank(message = "El numero debe ser mayor a cero")
    private int stock;

    private String imageUrl;
}
