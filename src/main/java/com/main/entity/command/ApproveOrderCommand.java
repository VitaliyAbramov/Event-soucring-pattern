package com.main.entity.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The command for an order approving process.
 */
@Data
@Builder
@AllArgsConstructor
public class ApproveOrderCommand {

    private Integer orderId;
    private Integer customerId;
}
