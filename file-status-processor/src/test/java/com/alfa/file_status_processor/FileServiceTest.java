package com.alfa.file_status_processor;

import com.alfa.file_status_processor.models.File;
import com.alfa.file_status_processor.repositories.FileRepository;
import com.alfa.file_status_processor.services.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceTest {
    @Test
    void getAndUpdate() {
        var repo = Mockito.mock(FileRepository.class);
        when(repo.findById("H")).thenReturn(Optional.of(new File("H","OK")));
        assertEquals("OK", new FileService(repo).getStatus("H"));
        new FileService(repo).updateStatus("H","X");
        verify(repo).save(any(File.class));
    }
}
