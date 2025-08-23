package com.alfa.file_processor.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatusProducer {
    private final KafkaTemplate<String, String> kafka;
    private final String topic;

    public StatusProducer(KafkaTemplate<String, String> kafka,
                          @Value("${kafka.topics.status}") String topic) {
        this.kafka = kafka;
        this.topic = topic;
    }

    public void send(String fileHash, String message) {
        kafka.send(topic, fileHash, message);
    }
}
