package com.example.aws_final.s3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket:mi-bucket-prueba}")
    private String bucketName;

    // Usamos @Qualifier porque tienes dos clientes (East y West)
    public S3Service(@Qualifier("s3ClientEast") S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String key, byte[] content) {
        try {
            // 1. Intentamos subir a AWS (Solo si tienes tus llaves listas)

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("image/jpeg") // Asumimos imagen por ahora, idealmente pasar content type
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));

            // 2. Retornamos la URL limpia (CORREGIDO: Sin el "String s3Url =")
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        } catch (Exception e) {
            // Si falla S3, imprimimos el error pero NO matamos la transacción
            System.err.println("Error subiendo ticket a S3: " + e.getMessage());
            return "URL_PENDIENTE_DE_NUBE";
        }
    }
}