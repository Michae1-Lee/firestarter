package com.alfa.file_uploader.exceptions;

public class WrongFormat extends RuntimeException{
    public WrongFormat(){
        super("Формат файла должен быть xls/xlsx");
    }
}
