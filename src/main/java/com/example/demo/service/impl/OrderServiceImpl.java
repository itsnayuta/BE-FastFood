package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setMemberId(updatedOrder.getMemberId());
                    existingOrder.setStatus(updatedOrder.getStatus());
                    existingOrder.setNote(updatedOrder.getNote());
                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getOrdersByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}
