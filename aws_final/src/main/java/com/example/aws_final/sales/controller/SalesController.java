package com.example.aws_final.sales.controller;

import com.example.aws_final.sales.dto.request.SaleRequestDto;
import com.example.aws_final.sales.dto.response.SaleResponseDto;
import com.example.aws_final.sales.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    /// PARA EL PDV necesitmaos tener un ID de retail
    @GetMapping("/all")
    public ResponseEntity<List<SaleResponseDto>> getAllSales() {
        return ResponseEntity.ok(salesService.getTodaySales());
    }

    @GetMapping("/user/{userId}/sales")
    public ResponseEntity<List<SaleResponseDto>> getAllSalesFromUser(
            @PathVariable("userId") int userId) {
        return ResponseEntity.ok(salesService.ListAllUserSales(userId));
    }

    @PostMapping("/sale")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SaleResponseDto> doSale(
            @RequestBody SaleRequestDto saleDto) {
        return ResponseEntity.ok(salesService.Sell(saleDto));
    }

}
