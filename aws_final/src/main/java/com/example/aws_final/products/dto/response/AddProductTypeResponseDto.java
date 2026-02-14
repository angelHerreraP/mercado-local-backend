package com.example.aws_final.products.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductTypeResponseDto {
    private String name; // regresas el nema
    private String message; /// Regresasd el clascio "Product type "..." se ha creado con Exiito."
}
