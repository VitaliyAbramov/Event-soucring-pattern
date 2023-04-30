package com.main.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.main.entity.dto.ItemDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * The event is responsible for the order-creating process.
 */
@Data
@Builder
public class OrderCreatedEvent {

    private Integer orderId;
    private Integer customerId;
    private List<ItemDto> items;

    private Integer amount;

    private final String eventType = "OrderCreatedEvent";

    @JsonCreator
    public OrderCreatedEvent(@JsonProperty("orderId") Integer orderId,
                             @JsonProperty("customerId") Integer customerId,
                             @JsonProperty("items") List<ItemDto> items,
                             @JsonProperty("amount") Integer amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.amount = amount;
    }
}
