package com.alfa.file_uploader.services;

import com.alfa.file_uploader.exceptions.FileIsTooLarge;
import com.alfa.file_uploader.exceptions.WrongFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class ValidationService {
    public void validate(MultipartFile file) {
        System.out.println(file.getContentType());
        if(!Objects.equals(file.getContentType(), "application/vnd.ms-excel") &&
                !Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            throw new WrongFormat();
        }
        else if(file.getSize() / 1024 / 1024 > 5){
            throw new FileIsTooLarge();
        }
    }
}
