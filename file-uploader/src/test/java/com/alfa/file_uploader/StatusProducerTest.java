package com.alfa.file_uploader;

import com.alfa.file_uploader.kafka.StatusProducer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class StatusProducerTest {
    @Test
    void sendsToKafka() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String,String> template = mock(KafkaTemplate.class);
        var prod = new StatusProducer(template, "status-topic");
        prod.send("k","v");
        verify(template).send("status-topic","k","v");
    }
}
