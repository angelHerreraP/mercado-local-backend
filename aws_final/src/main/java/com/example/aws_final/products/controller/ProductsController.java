package com.example.aws_final.products.controller;


import com.example.aws_final.products.dto.request.AddProductRequestDto;
import com.example.aws_final.products.dto.response.AddProductResponseDto;
import com.example.aws_final.products.dto.response.ProductResponseDto;
import com.example.aws_final.products.service.ProductService;
import com.example.aws_final.users.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    /// TODO: Review if this is necessary
    private final AuthService authService;


    /// Listamos todos los productois
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> listAllProducts(){
        return ResponseEntity.ok(productService.ListAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable int id) {
        // Asumiendo que tienes este método en el service
        return ResponseEntity.ok(productService.getProductById(id));
    }


    /// Crea y edita productos (this is wrong, buuuut it is what it is :D)
    @PostMapping("/add")
    @PreAuthorize("hasRole('SUPERUSER')") /// SOLO EL MERO MERO EDITA
    public ResponseEntity<AddProductResponseDto> addProduct(
            @RequestBody AddProductRequestDto addDto
    ){
        return new ResponseEntity<>(productService.addProduct(addDto), HttpStatus.CREATED);
    }


}
