package com.test.inventoryservice.service;

import com.test.inventoryservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    @RabbitListener(queues = "order.queue")
    public void receiveOrder(Order order) throws InterruptedException {
        log.info("Inventario: orden recibida -> {}", order);
        Thread.sleep(3000);

        boolean stock = new Random().nextBoolean();
        order.setStatus(stock ? "ACCEPTED" : "REJECTED");

        rabbitTemplate.convertAndSend("inventory.response.queue", order);

        if (stock) {
            rabbitTemplate.convertAndSend("delivery.request.queue", order);
            log.info("Inventario: orden aceptada y enviada a delivery-service -> {}", order);
        } else {
            log.warn("Inventario: orden rechazada por falta de stock -> {}", order);
        }
    }
}
