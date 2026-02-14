package com.example.aws_final.products.dto.response;


import com.example.aws_final.products.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductResponseDto {
    private String message; /// decimos si se agrego on creo
    private String productName; /// que jue
    private ProductType type; /// de que tipo fue
    private int stock; /// cuantos fueron
}
