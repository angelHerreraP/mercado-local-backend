package com.example.aws_final.sales.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductSummaryDto {

    private String name;
    private Integer quantity;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
