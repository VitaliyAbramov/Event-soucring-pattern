create database if not exists `example_db`;

create table `example_db`.`customer`
(
    `id`         int(4)      not null primary key auto_increment,
    `first_name` varchar(50) not null,
    `last_name`  varchar(50) not null,
    `email`      varchar(50) not null,
    `balance`    int(11)     not null
);

create table `example_db`.`orders`
(
    `id`          int(4)      not null primary key auto_increment,
    `order_id`    int(11)     NOT NULL,
    `customer_id` int(4)      not null,
    `status`      varchar(50) not null,
    `amount`      int(11)     not null,
    foreign key (`customer_id`) references `example_db`.`customer` (`id`)
);

create table `example_db`.`items`
(
    `id`           int(4)      not null primary key auto_increment,
    `product_id`   int(4)      not null,
    `product_name` varchar(50) not null,
    `price`        int(11)     not null
);

CREATE TABLE `example_db`.`order_items`
(
    `id`         int(4)  not null primary key auto_increment,
    `order_id`   int(4)  NOT NULL,
    `product_id` int(11) NOT NULL,
    `quantity`   int(11) NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `items` (`id`)
);

INSERT INTO `example_db`.`items` (product_id, product_name, price)
VALUES (1001, 'IPhone 14', 500),
       (2002, 'Lenovo IdeaPad 2022', 2500),
       (3003, 'Asus Zenbook', 2700),
       (4004, 'Baseus PowerCharger 20kMah', 100);

INSERT INTO `example_db`.`customer` (first_name, last_name, email, balance)
VALUES ('John', 'Doe', 'john.doe@example.com', 1000),
       ('Tom', 'Smith', 'tom.smith@example.com', 1000),
       ('Sara', 'Johnson', 'sara.johnson@example.com', 1000),
       ('Michael', 'Brown', 'michael.brown@example.com', 1000);
