package com.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.entity.OrderAggregate;
import com.main.entity.command.ApproveOrderCommand;
import com.main.entity.command.CreateOrderCommand;
import com.main.entity.command.PaymentProcessCommand;
import com.main.entity.dto.CreateOrderDto;
import com.main.handler.ApproveOrderCommandHandler;
import com.main.handler.CreateOrderCommandHandler;
import com.main.handler.PaymentProcessedCommandHandler;
import com.main.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * Order controller API.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final ApproveOrderCommandHandler approveOrderCommandHandler;
    private final PaymentProcessedCommandHandler paymentProcessedCommandHandler;
    private final OrderServiceImpl orderService;

    @PostMapping
    public void createOrder(@RequestBody CreateOrderDto request) throws JsonProcessingException {
        Random random = new Random();
        int orderId = random.ints(1, 10_000).findFirst().getAsInt();
        createOrderCommandHandler.handle(CreateOrderCommand.builder()
                .orderId(orderId)
                .customerId(request.getCustomerId())
                .items(request.getItems())
                .build());
    }

    @PostMapping("/{orderId}/confirm")
    public void confirmOrder(@PathVariable Integer orderId,
                             @RequestParam Integer customerId) throws JsonProcessingException {
        paymentProcessedCommandHandler.handle(PaymentProcessCommand.builder()
                .customerId(customerId)
                .orderId(orderId).build());
        approveOrderCommandHandler.handle(ApproveOrderCommand.builder()
                .customerId(customerId)
                .orderId(orderId).build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderAggregate> getOrder(@PathVariable Integer orderId) {
        OrderAggregate order = orderService.findOrderByHistory(orderId);
        return ObjectUtils.isEmpty(order) ? ResponseEntity.notFound().build()
                : new ResponseEntity<>(order, HttpStatus.OK);
    }
}
