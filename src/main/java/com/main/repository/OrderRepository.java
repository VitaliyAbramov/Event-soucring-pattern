package com.main.repository;

import com.main.entity.OrderAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderAggregate, Integer> {

    Optional<OrderAggregate> findByOrderId(Integer orderId);
}
