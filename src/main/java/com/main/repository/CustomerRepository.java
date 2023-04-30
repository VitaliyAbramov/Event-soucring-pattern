package com.main.repository;

import com.main.entity.CustomerAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerAggregate, Integer> {

}
