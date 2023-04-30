package com.main.handler;

import com.main.entity.CustomerAggregate;
import com.main.entity.OrderAggregate;
import com.main.entity.command.PaymentProcessCommand;
import com.main.repository.CustomerRepository;
import com.main.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handler for order payment process.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessedCommandHandler implements EventHandler<PaymentProcessCommand> {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void handle(PaymentProcessCommand command) {
        OrderAggregate order = orderRepository.findByOrderId(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        CustomerAggregate customer = customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        if (customer.getBalance() >= order.getAmount()) {
            customer.debit(orderRepository.findByOrderId(command.getOrderId()).get().getAmount());
        } else {
            throw new RuntimeException("Not enough money on your balance");
        }
        customerRepository.save(customer);
    }
}
