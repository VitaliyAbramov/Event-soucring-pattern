package com.main.service;

import com.main.entity.OrderAggregate;

/**
 * Order service.
 */
public interface OrderService {

    /**
     * Restores order state from event history.
     *
     * @param orderId order id
     * @return the order state
     */
    OrderAggregate findOrderByHistory(Integer orderId);
}
