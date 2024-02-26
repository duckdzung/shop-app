package com.example.shopapp.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.models.Order;

public interface IOrderService {
    Page<Order> getAllOrders(int page, int limit);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getOrderByUserId(Long userId);

    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order updateOrder(Long orderId, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long orderId) throws Exception;
}
