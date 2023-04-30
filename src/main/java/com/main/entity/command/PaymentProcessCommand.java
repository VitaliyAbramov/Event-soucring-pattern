package com.main.entity.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The command for an order payment process.
 */
@Data
@Builder
@AllArgsConstructor
public class PaymentProcessCommand {

    private Integer orderId;
    private Integer customerId;
}
