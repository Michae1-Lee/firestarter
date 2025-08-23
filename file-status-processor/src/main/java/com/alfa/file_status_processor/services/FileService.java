package com.alfa.file_status_processor.services;

import com.alfa.file_status_processor.models.File;
import com.alfa.file_status_processor.repositories.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String getStatus(String hash) {
        return fileRepository.findById(hash)
                .map(File::getStatus)
                .orElse(null);
    }

    public void updateStatus(String hash, String status) {
        fileRepository.save(new File(hash, status));
    }
}
