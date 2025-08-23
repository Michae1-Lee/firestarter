package com.alfa.file_processor.services;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ValidationService {

    public void validate(byte[] content) {
        try (Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(content))) {
            Sheet s = wb.getSheetAt(0);
            for (int r = 0; r < 2; r++) {
                Row row = s.getRow(r);
                if (row == null) throw new RuntimeException("Row " + (r+1) + " is empty");
                for (int c = 0; c < 3; c++) {
                    Cell cell = row.getCell(c);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        throw new RuntimeException("Cell [" + (r+1) + "," + (c+1) + "] is empty");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot read Excel: " + e.getMessage());
        }
    }

}
