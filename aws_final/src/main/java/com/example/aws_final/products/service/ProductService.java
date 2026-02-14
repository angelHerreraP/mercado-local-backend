package com.example.aws_final.products.service;

import com.example.aws_final.products.dto.request.AddProductRequestDto;
import com.example.aws_final.products.dto.response.AddProductResponseDto;
import com.example.aws_final.products.dto.response.AddProductTypeResponseDto;
import com.example.aws_final.products.dto.response.ProductResponseDto;
import com.example.aws_final.products.model.Product;
import com.example.aws_final.products.model.ProductType;
import com.example.aws_final.products.repository.ProductRepository;
import com.example.aws_final.products.repository.ProductTypeRepository;
import com.example.aws_final.shared.exception.AppException;
import com.example.aws_final.shared.exception.ErrorCodes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
        private final ProductRepository productRepository;
        private final ProductTypeRepository productTypeRepository;

        /// Add Prods
        @Transactional
        public AddProductResponseDto addProduct(AddProductRequestDto addProductDto) {
                // validamos que exista el tipo de producto (Solo notifica para logs)
                ProductType type = productTypeRepository.findByName(addProductDto.getProductType().getName())
                                .orElseGet(() -> {
                                        ProductType newType = ProductType.builder()
                                                        .name(addProductDto.getProductType().getName())
                                                        .build();
                                        return productTypeRepository.save(newType);
                                });

                /// Necesotas validar si ya existe un producto con ese Id on name, sis si,añade
                /// a Strock, sino crealo desde o0

                Optional<Product> productOpt = productRepository.findByName(addProductDto.getName());
                Product prod;

                if (productOpt.isPresent()) {
                        prod = productOpt.get();
                        prod.setStock(prod.getStock() + addProductDto.getStock());
                        // Update other fields if provided (Upsert-like behavior)
                        prod.setPrecio(addProductDto.getPrecio());
                        prod.setDescripcion(addProductDto.getDescription());
                        if (addProductDto.getImageUrl() != null && !addProductDto.getImageUrl().isEmpty()) {
                                prod.setImageUrl(addProductDto.getImageUrl());
                        }
                        prod.setUpdatedAt(Instant.now());
                } else {
                        // CASO B: Es nuevo -> Lo creamos de cero (Insert)
                        prod = Product.builder()
                                        .name(addProductDto.getName())
                                        .descripcion(addProductDto.getDescription())
                                        .precio(addProductDto.getPrecio())
                                        .stock(addProductDto.getStock())
                                        .imageUrl(addProductDto.getImageUrl())
                                        .type(type) // Usamos el que encontramos o creamos arriba
                                        .build();
                }

                productRepository.save(prod);

                return AddProductResponseDto.builder()
                                .message("Gestión de inventario exitosa")
                                .productName(prod.getName())
                                .type(prod.getType())
                                .stock(prod.getStock())
                                .build();
        }

        @Transactional // Usamos readOnly porque solo es consulta, es más rápido
        public ProductResponseDto getProductById(int id) {
                // 1. Buscamos el producto en la DB
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND)); // O el código que uses para
                                                                                            // 404

                // 2. Lo convertimos a DTO (puedes usar un mapper o el builder manual)
                return ProductResponseDto.builder()
                                .id(product.getId())
                                .productName(product.getName())
                                .description(product.getDescripcion())
                                .stock(product.getStock())
                                .price(product.getPrecio())
                                .type(product.getType()) // Asumiendo que tu DTO acepta el objeto ProductType
                                .imageUrl(product.getImageUrl())
                                .build();
        }

        /// List All your Prods
        public List<ProductResponseDto> ListAllProducts() {
                List<Product> products = productRepository.findAll();

                return products.stream()
                                .map(product -> ProductResponseDto.builder()
                                                .id(product.getId())
                                                .productName(product.getName())
                                                .description(product.getDescripcion())
                                                .type(product.getType())
                                                .stock(product.getStock())
                                                .price(product.getPrecio())
                                                .imageUrl(product.getImageUrl())
                                                .build())
                                .collect(Collectors.toList());
        }

}
