package com.test.orderservice.dto;

import lombok.Data;

@Data
public class OrderMessage {
    private Long id;
    private String item;
    private Integer quantity;
    private String status;
}