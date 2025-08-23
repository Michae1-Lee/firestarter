package com.alfa.file_uploader.controllers;

import com.alfa.file_uploader.dto.FileStatus;
import com.alfa.file_uploader.exceptions.FileIsTooLarge;
import com.alfa.file_uploader.exceptions.WrongFormat;
import com.alfa.file_uploader.http.StatusClient;
import com.alfa.file_uploader.kafka.StatusProducer;
import com.alfa.file_uploader.kafka.UploadProducer;
import com.alfa.file_uploader.services.HashService;
import com.alfa.file_uploader.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.alfa.file_uploader.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {

    private final ValidationService validationService;
    private final HashService hashService;
    private final StatusProducer statusProducer;
    private final UploadProducer uploadProducer;
    private final StatusClient statusClient;

    @Autowired
    public UploadController(ValidationService validationService, HashService hashService,
                            StatusProducer statusProducer, UploadProducer uploadProducer,
                            StatusClient statusClient) {
        this.validationService = validationService;
        this.hashService = hashService;
        this.statusProducer = statusProducer;
        this.uploadProducer = uploadProducer;
        this.statusClient = statusClient;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        final String fileHash = hashService.hash(file);

        String existingStatus = statusClient.getStatus(fileHash);

        if (existingStatus != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("File with hash " + fileHash + " already exists, status=" + existingStatus);
        }

        statusProducer.send(fileHash, FileStatus.ACCEPTED.name());

        try {
            validationService.validate(file);
            statusProducer.send(fileHash, FileStatus.FIRST_VALIDATION_PASSED.name());
            uploadProducer.send(fileHash, file.getBytes());
            return ResponseEntity.ok(fileHash);
        } catch (WrongFormat | FileIsTooLarge e) {
            statusProducer.send(fileHash, FileStatus.FIRST_VALIDATION_FAILED.name());
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler(FileIsTooLarge.class)
    public ResponseEntity<ErrorResponse> handleException(FileIsTooLarge e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("FileIsTooLarge", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(WrongFormat e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("WrongFormat", e.getMessage()));
    }
}
