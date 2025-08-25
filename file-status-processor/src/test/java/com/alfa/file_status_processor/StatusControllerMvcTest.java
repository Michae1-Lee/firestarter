package com.alfa.file_status_processor;

import com.alfa.file_status_processor.controllers.StatusController;
import com.alfa.file_status_processor.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StatusControllerMvcTest {

    private MockMvc mvc;

    @Mock
    private FileService service;

    private StatusController controller;

    @BeforeEach
    void setUp() {
        controller = new StatusController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void okWhenFound() throws Exception {
        when(service.getStatus("H1")).thenReturn("OK");
        mvc.perform(get("/status/H1"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void notFound() throws Exception {
        when(service.getStatus("X")).thenReturn(null);
        mvc.perform(get("/status/X"))
                .andExpect(status().isNotFound());
    }
}