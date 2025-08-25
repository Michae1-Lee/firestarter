package com.alfa.file_status_processor;

import com.alfa.file_status_processor.repositories.FileRepository;
import com.alfa.file_status_processor.models.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "spring.main.allow-bean-definition-overriding=true",

        "spring.kafka.consumer.group-id=status-consumer",
        "spring.kafka.consumer.auto-offset-reset=earliest",
        "spring.kafka.listener.missing-topics-fatal=false",

        "kafka.topics.status=status-topic"
})
class StatusConsumerIT {

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @Container
    static MongoDBContainer mongo = new MongoDBContainer(
            DockerImageName.parse("mongo:7.0"));

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        r.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);

        r.add("kafka.bootstrap-servers", kafka::getBootstrapServers);
        r.add("app.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired KafkaTemplate<String, String> kafkaTemplate;
    @Autowired FileRepository fileRepository;

    @Test
    void statusMessage_isConsumed_andSavedToMongo() {
        String topic = "status-topic";
        String hash = "H999";
        String status = "PROCESSED";

        kafkaTemplate.send(topic, hash, status);

        await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
            var saved = fileRepository.findById(hash);
            assertThat(saved).isPresent();
            assertThat(saved.get().getStatus()).isEqualTo(status);
        });
    }

    @Test
    void multipleStatusUpdates_lastOneWins() {
        String topic = "status-topic";
        String hash = "H777";

        kafkaTemplate.send(topic, hash, "VALIDATING");
        kafkaTemplate.send(topic, hash, "PROCESSED");

        await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
            File f = fileRepository.findById(hash).orElseThrow();
            assertThat(f.getStatus()).isEqualTo("PROCESSED");
        });
    }
}
