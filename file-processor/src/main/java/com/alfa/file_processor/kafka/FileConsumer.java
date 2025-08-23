package com.alfa.file_processor.kafka;


import com.alfa.file_processor.services.ValidationService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class FileConsumer {

    private final ValidationService validationService;
    private final StatusProducer statusProducer;

    public FileConsumer(ValidationService validationService, StatusProducer statusProducer) {
        this.validationService = validationService;
        this.statusProducer = statusProducer;
    }

    @KafkaListener(
            topics = "${kafka.topics.upload}",
            groupId = "file-consumer",
            properties = {
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG + "=org.apache.kafka.common.serialization.StringDeserializer",
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG + "=org.apache.kafka.common.serialization.ByteArrayDeserializer"
            }
    )
    public void consume(byte[] content,
                        @Header(KafkaHeaders.RECEIVED_KEY) String fileHash) {
        System.out.println("Received file hash=" + fileHash + " size=" + content.length);
        try {
            validationService.validate(content);
            statusProducer.send(fileHash, "SECOND_VALIDATION_PASSED");
        } catch (RuntimeException e) {
            statusProducer.send(fileHash, "SECOND_VALIDATION_FAILED: " + e.getMessage());
        } catch (Exception e) {
            statusProducer.send(fileHash, "SECOND_VALIDATION_FAILED: internal error");
        }
    }
}

