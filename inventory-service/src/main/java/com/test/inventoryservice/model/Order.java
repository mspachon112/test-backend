package com.test.inventoryservice.model;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private String item;
    private Integer quantity;
    private String status;
}