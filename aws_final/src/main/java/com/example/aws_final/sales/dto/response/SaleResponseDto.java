package com.example.aws_final.sales.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SaleResponseDto {
    private int userId;
    private List<ProductSummaryDto> products;
    private BigDecimal total;
    private String timestamp; //Cuando se ejecuto
    private String ticketUrl;
    private String message;

}
