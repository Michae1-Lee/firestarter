package com.alfa.file_processor;

import com.alfa.file_processor.kafka.StatusProducer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class StatusProducerTest {
    @Test
    void sendsStatus() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String,String> template = mock(KafkaTemplate.class);
        var prod = new StatusProducer(template, "status-topic");
        prod.send("k","v");
        verify(template).send("status-topic","k","v");
    }
}
