package com.example.aws_final.sales.service;

import com.example.aws_final.products.model.Product;
import com.example.aws_final.products.repository.ProductRepository;
import com.example.aws_final.s3.service.DynamicS3Service;
import com.example.aws_final.s3.service.S3Service;
import com.example.aws_final.sales.dto.request.SaleRequestDto;
import com.example.aws_final.sales.dto.response.ProductSummaryDto;
import com.example.aws_final.sales.dto.response.SaleResponseDto;
import com.example.aws_final.sales.model.Sale;
import com.example.aws_final.sales.repository.SaleRepository;
import com.example.aws_final.shared.exception.AppException;
import com.example.aws_final.shared.exception.ErrorCodes;
import com.example.aws_final.users.model.User;
import com.example.aws_final.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesService {
    private final S3Service s3Service;
    private final DynamicS3Service dynamicS3Service;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public SaleResponseDto Sell(SaleRequestDto saleRequestDto) {
        // 1. Obtener Usuario
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND));

        // Validar que no venga vacío
        if (saleRequestDto.getItems() == null || saleRequestDto.getItems().isEmpty()) {
            throw new AppException(ErrorCodes.EMPTY_PRODUCTS);
        }

        BigDecimal totalVenta = BigDecimal.ZERO;
        List<ProductSummaryDto> ticketDetails = new ArrayList<>();
        StringBuilder ticketBuilder = new StringBuilder();
        ticketBuilder.append("--- MERCADOLOCAL Mx ---\n");
        ticketBuilder.append("Cajero: ").append(username).append("\n\n");

        for (var itemRequest : saleRequestDto.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new AppException(ErrorCodes.EMPTY_STOCK);
            }

            BigDecimal subtotal = product.getPrecio().multiply(new BigDecimal(itemRequest.getQuantity()));
            totalVenta = totalVenta.add(subtotal);

            // Actualizar Stock en RDS
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            // GUARDAR CADA LÍNEA DE VENTA
            Sale saleRecord = Sale.builder()
                    .user(user)
                    .product(product)
                    .cantidad(itemRequest.getQuantity())
                    .precioUnitario(product.getPrecio())
                    .total(subtotal)
                    // .saleDate(Instant.now()) // Solo si quitas el 'insertable=false'
                    .build();

            saleRepository.save(saleRecord);

            // Preparar info para el Ticket y la Respuesta
            ticketBuilder.append(String.format("%s x%d ... $%s\n",
                    product.getName(), itemRequest.getQuantity(), subtotal));

            ticketDetails.add(ProductSummaryDto.builder()
                    .name(product.getName())
                    .quantity(itemRequest.getQuantity())
                    .precioUnitario(product.getPrecio())
                    .subtotal(subtotal)
                    .build());
        }

        ticketBuilder.append("\n--------------------------\n");
        ticketBuilder.append("TOTAL: $").append(totalVenta);

        String s3Url;
        // 3. SUBIR A S3 (Respaldo en la nube)
        String fileName = "tickets/sale-" + System.currentTimeMillis() + ".txt";
        try {
            s3Url = s3Service.uploadFile(fileName, ticketBuilder.toString().getBytes());
        } catch (Exception e) {
            // Si falla S3, el @Transactional hace que el stock NO baje. ¡Magia!
            throw new AppException(ErrorCodes.INTERNAL_SERVER_ERROR);
        }

        return SaleResponseDto.builder()
                .userId(user.getId())
                .total(totalVenta)
                .products(ticketDetails)
                .ticketUrl(s3Url)
                .timestamp(Instant.now().toString())
                .message("Venta generada y respaldada en S3 con éxito")
                .build();
    }


    //Dame las vistas por empleado
    public List<SaleResponseDto> ListAllUserSales(Integer userId){
        List<Sale> salesFromEmployee = saleRepository.findByUserId(userId);
        return salesFromEmployee.stream()
                .map(this::mapToResponseDto) // Un método helper para no repetir código
                .collect(Collectors.toList());
    }

    //Dame las ventas diarias
    public List<SaleResponseDto> getTodaySales(){
        //Get inicio y fin del dia
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant now = Instant.now();

        List<Sale> sales = saleRepository.findBySaleDateBetweenOrderBySaleDateDesc(startOfDay, now);
        return sales.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }




    private SaleResponseDto mapToResponseDto(Sale sale) {
        return SaleResponseDto.builder()
                .userId(sale.getUser().getId())
                .total(sale.getTotal())
                .timestamp(sale.getSaleDate().toString())
                .message("Historial recuperado")
                .build();
    }
}
