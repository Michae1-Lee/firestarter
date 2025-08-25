package com.alfa.file_uploader;

import com.alfa.file_uploader.http.StatusClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StatusClientTest {
    @Test
    void returnsBody() {
        var rt = mock(RestTemplate.class);
        when(rt.getForEntity(anyString(), eq(String.class), anyString())).thenReturn(ResponseEntity.ok("OK"));
        assertEquals("OK", new StatusClient(rt).getStatus("h"));
    }
    @Test
    void returnsNullOnException() {
        var rt = mock(RestTemplate.class);
        when(rt.getForEntity(anyString(), eq(String.class), anyString())).thenThrow(new RuntimeException());
        assertNull(new StatusClient(rt).getStatus("h"));
    }
}
