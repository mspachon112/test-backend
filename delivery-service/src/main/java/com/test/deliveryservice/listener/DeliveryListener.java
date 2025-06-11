package com.test.deliveryservice.listener;

import com.test.deliveryservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryListener {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "delivery.request.queue")
    public void handleDeliveryRequest(Order order) throws InterruptedException {
        log.info("Delivery: solicitud recibida para procesar orden -> {}", order);
        Thread.sleep(3000);

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getId());
        response.put("status", "DELIVERY_CREATED");

        rabbitTemplate.convertAndSend("delivery.response.queue", response);
        log.info("Delivery: orden entregada con éxito. Respuesta enviada -> {}", order);
    }
}