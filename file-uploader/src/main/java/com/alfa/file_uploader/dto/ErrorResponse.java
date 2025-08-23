package com.alfa.file_uploader.dto;

public class ErrorResponse {
    private String error;       // тип ошибки
    private String description; // описание

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}