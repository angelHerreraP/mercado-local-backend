package com.example.aws_final.s3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DynamicS3Service {
    @Value("${aws.s3.bucket:nombre-generico-bucket}")
    private String bucketName;
    // Este mapa guarda los clientes de cada región para no crearlos mil veces
    private final Map<Region, S3Client> clientCache = new ConcurrentHashMap<>();

    public String uploadToRegion(String regionName, String key, byte[] content) {
        Region region = Region.of(regionName);

        S3Client client = clientCache.computeIfAbsent(region, r ->
                S3Client.builder()
                        .region(r)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build()
        );

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key) // Ej: "tickets/venta-1.txt"
                .contentType("text/plain")
                .build();

        // 3. Ejecutar la subida real a AWS
        client.putObject(putObjectRequest, RequestBody.fromBytes(content));

        // 4. CONSTRUIR LA URL (Lo que no entendías)
        // Esta es la dirección que Angular usará para ver el archivo.
        // Formato: https://NOMBRE_BUCKET.s3.REGION.amazonaws.com/NOMBRE_ARCHIVO
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, regionName, key);
    }
}