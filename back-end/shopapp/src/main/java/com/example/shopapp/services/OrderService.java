package com.example.shopapp.services;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.services.interfaces.IOrderService;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public Page<Order> getAllOrders(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<Order> orders = orderRepository.findAll(pageRequest);

        return orders;
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order with ID: " + orderId));

        return order;
    }

    @Override
    public List<Order> getOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders;
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        // Check if user exists
        Long userId = orderDTO.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with ID: " + userId));

        // Convert DTO to object and set some fields
        Order order = modelMapper.map(orderDTO, Order.class);
        String trackingNumber = UUID.randomUUID().toString();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus("pending");
        order.setTrackingNumber(trackingNumber);
        order.setIsActive(true);

        // Save to DB
        order = orderRepository.save(order);

        return order;
    }

    @Override
    public Order updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        // Check if order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with ID: " + orderId));

        // Check if user exists
        Long userId = orderDTO.getUserId();
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with ID: " + userId));

        // Convert DTO to object and set some fields
        order = modelMapper.map(orderDTO, Order.class);
        order.setId(orderId);
        order.setUser(user);

        // Save to DB
        order = orderRepository.save(order);

        return order;
    }

    @Override
    public void deleteOrder(Long orderId) throws Exception {
        Order order = getOrderById(orderId);
        order.setIsActive(false);
        orderRepository.save(order);
    }
}
