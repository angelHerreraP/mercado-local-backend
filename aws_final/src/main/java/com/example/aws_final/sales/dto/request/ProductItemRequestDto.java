package com.example.aws_final.sales.dto.request;

import lombok.Data;

@Data
public class ProductItemRequestDto {
    @jakarta.validation.constraints.NotNull
    private Integer productId;

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Min(value = 1)
    private Integer quantity;
}
