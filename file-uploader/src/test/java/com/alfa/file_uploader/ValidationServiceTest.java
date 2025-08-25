package com.alfa.file_uploader;

import com.alfa.file_uploader.exceptions.FileIsTooLarge;
import com.alfa.file_uploader.exceptions.WrongFormat;
import com.alfa.file_uploader.services.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {
    ValidationService validation = new ValidationService();

    @Test
    void ok_whenXlsOrXlsxAndSmall() {
        var xls = new MockMultipartFile("f","a.xls","application/vnd.ms-excel", new byte[1024]);
        var xlsx = new MockMultipartFile("f","a.xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[1024]);
        assertDoesNotThrow(() -> validation.validate(xls));
        assertDoesNotThrow(() -> validation.validate(xlsx));
    }

    @Test
    void error_whenWrongType() {
        var bad = new MockMultipartFile("f","a.txt","text/plain","hi".getBytes());
        assertThrows(WrongFormat.class, () -> validation.validate(bad));
    }

    @Test
    void error_whenTooLarge() {
        var big = new MockMultipartFile("f","a.xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[6*1024*1024]);
        assertThrows(FileIsTooLarge.class, () -> validation.validate(big));
    }
}
