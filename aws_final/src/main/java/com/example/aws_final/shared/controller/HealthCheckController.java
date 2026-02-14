package com.example.aws_final.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthCheckController {

    private final DataSource dataSource; // Esto lo inyecta Spring sí o sí

    @GetMapping
    public ResponseEntity<Map<String, Object>> check() {
        Map<String, Object> status = new TreeMap<>();
        status.put("timestamp", System.currentTimeMillis());

        // Intentamos abrir una conexión y ejecutar un SELECT 1
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeQuery("SELECT 1"); // Si el RDS está muerto, aquí truena

            status.put("status", "UP");
            status.put("database", "CONNECTED");
            return ResponseEntity.ok(status);

        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("database", "DISCONNECTED");
            status.put("error", "RDS no responde: " + e.getMessage());

            // 503 le dice a AWS: "¡Quítame del Load Balancer, estoy fallando!"
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
        }
    }
}