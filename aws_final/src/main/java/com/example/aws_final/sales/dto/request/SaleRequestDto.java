package com.example.aws_final.sales.dto.request;


import lombok.Data;

import java.util.List;

@Data
public class SaleRequestDto {
    private List<ProductItemRequestDto>items;
}


