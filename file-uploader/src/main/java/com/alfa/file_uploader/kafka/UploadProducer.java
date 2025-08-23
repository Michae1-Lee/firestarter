package com.alfa.file_uploader.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UploadProducer {
    private final KafkaTemplate<String, byte[]> kafka;
    private final String topic;

    public UploadProducer(
                          @Value("${kafka.topics.upload}") String topic) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);

        ProducerFactory<String, byte[]> pf = new DefaultKafkaProducerFactory<>(props);
        this.kafka = new KafkaTemplate<>(pf);
        this.topic = topic;
    }

    public void send(String fileHash, byte[] content) {
        kafka.send(topic, fileHash, content);
    }
}
