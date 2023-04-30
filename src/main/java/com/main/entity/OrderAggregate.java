package com.main.entity;

import com.main.event.OrderApprovedEvent;
import com.main.event.OrderCreatedEvent;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Order aggregate entity that applies all events.
 */
@Data
@Entity
@Table(name = "orders")
public class OrderAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "customer_id")
    private Integer customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "amount")
    private Integer amount;

    public OrderAggregate() {
        this.status = OrderStatus.NEW;
    }

    public void apply(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.customerId = event.getCustomerId();
        this.amount = event.getAmount();
        this.status = OrderStatus.CREATED;
    }

    public void apply(OrderApprovedEvent event) {
        this.orderId = event.getOrderId();
        this.customerId = event.getCustomerId();
        this.status = OrderStatus.APPROVED;
    }
}
