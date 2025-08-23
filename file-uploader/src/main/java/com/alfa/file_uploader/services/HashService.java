package com.alfa.file_uploader.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.util.HexFormat;

@Service
public class HashService {
    public String hash(MultipartFile file){
        try {
            byte[] hashBytes = MessageDigest.getInstance("SHA-256").digest(file.getBytes());
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось вычислить хэш файла", e);
        }
    }
}
