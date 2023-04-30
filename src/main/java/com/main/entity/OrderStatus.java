package com.main.entity;

/**
 * List of order statuses.
 * Now not all statuses are used. The rest of them are planned to use in the next releases.
 */
public enum  OrderStatus {
    NEW,
    CREATED,
    APPROVED,
    SHIPPED,
    DELIVERED,
    CANCELED
}
