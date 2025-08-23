package com.alfa.file_status_processor.controllers;

import com.alfa.file_status_processor.models.File;
import com.alfa.file_status_processor.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController{

    private final FileService fileService;

    @Autowired
    public StatusController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(path = "/status/{hash}")
    public ResponseEntity<String> status(@PathVariable String hash) {
        String status = fileService.getStatus(hash);
        return (status == null) ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(status);
    }

}

