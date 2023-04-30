package com.main.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.entity.OrderAggregate;
import com.main.event.OrderApprovedEvent;
import com.main.event.OrderCreatedEvent;
import com.main.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer responsible for consuming the order events from kafka.
 */
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    /**
     * Catching the events from kafka related to order state.
     *
     * @param record consumer record
     * @throws JsonProcessingException possible exception
     */
    @KafkaListener(topics = "${spring.kafka.topics.order.name}", groupId = "${spring.kafka.consumer.group-id}")
    private void onOrderCreated(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String key = record.key();
        String value = record.value();
        Integer orderId = Integer.parseInt(key);

        JsonNode eventNode = objectMapper.readTree(value);
        String eventType = eventNode.get("eventType").asText();
        if ("OrderCreatedEvent".equals(eventType)) {
            OrderCreatedEvent event = objectMapper.readValue(value, OrderCreatedEvent.class);
            OrderAggregate order = new OrderAggregate();
            order.apply(event);
            orderRepository.save(order);
        } else if ("OrderApprovedEvent".equals(eventType)) {
            OrderApprovedEvent event = objectMapper.readValue(value, OrderApprovedEvent.class);
            OrderAggregate order = orderRepository.findByOrderId(orderId).orElseThrow(
                    () -> new IllegalArgumentException("Order with id " + orderId + " not found"));
            order.apply(event);
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
}
