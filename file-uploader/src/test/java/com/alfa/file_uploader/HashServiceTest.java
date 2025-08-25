package com.alfa.file_uploader;

import com.alfa.file_uploader.services.HashService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.security.MessageDigest;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashServiceTest {
    @Test
    void calculatesSha256() throws Exception {
        byte[] bytes = "hello".getBytes();
        var file = new MockMultipartFile("f","hello.txt","text/plain", bytes);
        String expected = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        String actual = new HashService().hash(file);
        assertEquals(expected, actual);
    }
}
