package com.example.demo.service;

import com.example.demo.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Order order);
    Optional<Order> getOrderById(Long id);
    List<Order> getOrdersByMemberId(Long memberId);
    List<Order> getAllOrders();
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
}
