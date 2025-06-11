package com.test.orderservice.listener;

import com.test.orderservice.model.Order;
import com.test.orderservice.model.OrderStatus;
import com.test.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListener {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = "inventory.response.queue")
    public void handleInventoryResponse(Map<String, Object> data) {
        try {
            Thread.sleep(3000);
            Long id = Long.valueOf(data.get("id").toString());
            String status = data.get("status").toString();

            Order order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                order.setStatus(OrderStatus.valueOf(status));
                orderRepository.save(order);
                log.info("Inventario actualizó el estado de la orden (ID: {}) a: {}", id, status);
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "delivery.response.queue")
    public void handleDeliveryResponse(Map<String, Object> data) {
        try {
            Thread.sleep(5000);
            Long id = Long.valueOf(data.get("id").toString());
            String status = data.get("status").toString();

            Order order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                order.setStatus(OrderStatus.valueOf(status));
                orderRepository.save(order);
                log.info("Delivery actualizó el estado de la orden (ID: {}) a: {}", id, status);
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje de delivery: " + e.getMessage());
        }
    }
}