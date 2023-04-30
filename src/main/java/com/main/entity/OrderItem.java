package com.main.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Intermediate entity between orders and items.
 */
@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderAggregate order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Item item;

    @Column(name = "quantity")
    private Integer quantity;
}
