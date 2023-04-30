package com.main.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Customer aggregate entity that applies all events.
 */
@Data
@Entity
@Table(name = "customer")
public class CustomerAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "balance")
    private Integer balance;

    public CustomerAggregate() {
        balance = 1000;
    }

    public void debit(Integer amount) {
        balance -= amount;
    }

    public void credit(Integer amount) {
        balance += amount;
    }
}
