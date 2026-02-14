package com.example.aws_final.products.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddProductTypeRequestDto {

    @NotBlank(message = "El nombre del tipo no puede estar vacio.")
    private String name;
}
