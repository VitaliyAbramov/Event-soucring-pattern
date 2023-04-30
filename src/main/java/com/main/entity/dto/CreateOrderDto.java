package com.main.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO for order short info.
 */
@Data
public class CreateOrderDto {

    private Integer customerId;
    private List<ItemDto> items;
}
