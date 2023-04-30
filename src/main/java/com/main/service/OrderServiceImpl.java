package com.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.entity.OrderAggregate;
import com.main.event.OrderApprovedEvent;
import com.main.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order service implementation.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ConsumerFactory<String, Object> consumerFactory;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.order.name}")
    private String orderTopic;

    @Value("${spring.kafka.topics.order.partitions}")
    private int numPartitions;

    @SneakyThrows
    @Override
    public OrderAggregate findOrderByHistory(Integer orderId) {
        List<Object> events = loadEventsFromKafka(orderId);
        OrderAggregate order = new OrderAggregate();
        for (Object event : events) {
            if (event instanceof OrderCreatedEvent) {
                order.apply((OrderCreatedEvent) event);
            } else if (event instanceof OrderApprovedEvent) {
                order.apply((OrderApprovedEvent) event);
            }
        }
        return order;
    }

    private List<Object> loadEventsFromKafka(Integer orderId) throws JsonProcessingException {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerFactory.getConfigurationProperties())) {
            TopicPartition topicPartition = new TopicPartition(orderTopic, orderId.hashCode() % numPartitions);
            consumer.assign(Collections.singletonList(topicPartition));
            consumer.seekToBeginning(Collections.singletonList(topicPartition));
            List<Object> events = new ArrayList<>();
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                if (orderId.toString().equals(record.key())) {
                    JsonNode eventNode = objectMapper.readTree(record.value());
                    String eventType = eventNode.get("eventType").asText();
                    if ("OrderCreatedEvent".equals(eventType)) {
                        events.add(objectMapper.readValue(record.value(), OrderCreatedEvent.class));
                    } else if ("OrderApprovedEvent".equals(eventType)) {
                        events.add(objectMapper.readValue(record.value(), OrderApprovedEvent.class));
                    }
                }
            }
            return events;
        }
    }
}
