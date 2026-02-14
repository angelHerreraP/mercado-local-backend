package com.example.aws_final.sales.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SaleRequestDto {
    @jakarta.validation.constraints.NotEmpty
    @jakarta.validation.Valid
    private List<ProductItemRequestDto> items;
}
