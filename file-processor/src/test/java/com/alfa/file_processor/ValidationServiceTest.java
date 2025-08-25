package com.alfa.file_processor;

import com.alfa.file_processor.services.ValidationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceTest {
    @Test
    void invalidExcelBytesCauseError() {
        byte[] bad = new byte[] {1,2,3,4,5};
        assertThrows(RuntimeException.class, () -> new ValidationService().validate(bad));
    }
}
