package com.test.deliveryservice.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Order implements Serializable {
    private Long id;
    private String item;
    private Integer quantity;
    private String status;
}