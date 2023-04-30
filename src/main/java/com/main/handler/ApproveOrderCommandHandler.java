package com.main.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.entity.command.ApproveOrderCommand;
import com.main.event.OrderApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Handler for an order approving process.
 */
@Component
@RequiredArgsConstructor
public class ApproveOrderCommandHandler implements EventHandler<ApproveOrderCommand>{

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.order.name}")
    private String orderTopic;

    @Override
    public void handle(ApproveOrderCommand command) throws JsonProcessingException {
        OrderApprovedEvent event = OrderApprovedEvent.builder()
                .customerId(command.getCustomerId())
                .orderId(command.getOrderId())
                .build();
        kafkaTemplate.send(orderTopic, String.valueOf(command.getOrderId()),
                objectMapper.writeValueAsString(event));
    }
}
