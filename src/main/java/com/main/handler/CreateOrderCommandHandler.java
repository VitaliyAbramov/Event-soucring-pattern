package com.main.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.entity.Item;
import com.main.entity.command.CreateOrderCommand;
import com.main.entity.dto.ItemDto;
import com.main.event.OrderCreatedEvent;
import com.main.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handler for order creating process.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements EventHandler<CreateOrderCommand>{

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ItemRepository itemRepository;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.order.name}")
    private String orderTopic;

    @Override
    public void handle(CreateOrderCommand command) throws JsonProcessingException {
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(command.getOrderId())
                .customerId(command.getCustomerId())
                .items(command.getItems())
                .amount(mapToAmount(command.getItems()))
                .build();
        kafkaTemplate.send(orderTopic, String.valueOf(command.getOrderId()),
                objectMapper.writeValueAsString(event));
    }

    /**
     * Calculate common order amount.
     *
     * @param items list of ordered items
     * @return the order amount
     */
    private Integer mapToAmount(List<ItemDto> items) {
        return items.stream()
                .mapToInt(itemDto -> {
                    Integer productId = itemDto.getProductId();
                    Integer quantity = itemDto.getQuantity();
                    Item item = itemRepository.findItemByProductId(productId)
                            .orElseThrow(() -> new IllegalArgumentException("Item with id " + productId + " not found"));
                    return item.getPrice() * quantity;
                })
                .sum();
    }
}
