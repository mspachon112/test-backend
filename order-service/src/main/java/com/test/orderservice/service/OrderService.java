package com.test.orderservice.service;

import com.test.orderservice.dto.OrderMessage;
import com.test.orderservice.dto.OrderRequest;
import com.test.orderservice.model.Order;
import com.test.orderservice.model.OrderStatus;
import com.test.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public Order createOrder(OrderRequest request) {
        Order order = new Order(null, request.getItem(), request.getQuantity(), OrderStatus.PENDING);
        Order saved = orderRepository.save(order);

        OrderMessage message = new OrderMessage();
        message.setId(saved.getId());
        message.setItem(saved.getItem());
        message.setQuantity(saved.getQuantity());
        message.setStatus(saved.getStatus().name());

        rabbitTemplate.convertAndSend("order.queue", message);

        return saved;
    }

    public List<Order> consultAll(){
        return orderRepository.findAll();
    }

    public Order consultOrder(Long idOrder) {
        return orderRepository.findById(idOrder)
                .orElseThrow(() -> new NoSuchElementException("Orden no econtrada con el ID " + idOrder));
    }

}
