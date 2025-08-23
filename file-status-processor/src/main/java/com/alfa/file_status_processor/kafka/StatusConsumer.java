package com.alfa.file_status_processor.kafka;

import com.alfa.file_status_processor.services.FileService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class StatusConsumer {

    private final FileService statusService;

    public StatusConsumer(FileService statusService) {
        this.statusService = statusService;
    }

    @KafkaListener(
            topics = "${kafka.topics.status}",
            groupId = "status-consumer"
    )

    public void onStatus(String status,
                         @Header(KafkaHeaders.RECEIVED_KEY) String hash) {
        statusService.updateStatus(hash, status);
    }
}

