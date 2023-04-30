package com.main.entity.command;

import com.main.entity.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * The command for an order-creating process.
 */
@Data
@Builder
@AllArgsConstructor
public class CreateOrderCommand {

    private Integer orderId;
    private Integer customerId;
    private List<ItemDto> items;
}
