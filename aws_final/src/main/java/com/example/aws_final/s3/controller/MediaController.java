package com.example.aws_final.s3.controller;

import com.example.aws_final.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String key = "products/" + UUID.randomUUID().toString() + extension;

            String url = s3Service.uploadFile(key, file.getBytes());

            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            response.put("key", key);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error processing file upload"));
        }
    }
}
