package com.alfa.file_uploader.exceptions;

public class FileIsTooLarge extends RuntimeException {
    public FileIsTooLarge() {
        super("Размер файла не должен превышать 5мб");
    }
}
