package com.main.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * The event is responsible for the order approval process.
 */
@Data
@Builder
public class OrderApprovedEvent {

    private Integer orderId;
    private Integer customerId;

    private final String eventType = "OrderApprovedEvent";

    @JsonCreator
    public OrderApprovedEvent(@JsonProperty("orderId") Integer orderId,
                              @JsonProperty("customerId") Integer customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }
}
