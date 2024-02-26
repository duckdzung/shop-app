package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.example.shopapp.repositories.OrderDetailRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.services.interfaces.IOrderDetailService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {
        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private OrderDetailRepository orderDetailRepository;

        @Autowired
        private ProductRepository productRepository;

        private ModelMapper modelMapper = new ModelMapper();

        @Override
        public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
                // Check if order exists
                Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                                .orElseThrow(() -> new DataNotFoundException(
                                                "Cannot find order with ID : " + orderDetailDTO.getOrderId()));

                // Check if product exists
                Product product = productRepository.findById(orderDetailDTO.getProductId())
                                .orElseThrow(() -> new DataNotFoundException(
                                                "Cannot find product with ID: " + orderDetailDTO.getProductId()));

                // Convert from DTO to model
                OrderDetail orderDetail = modelMapper.map(orderDetailDTO, OrderDetail.class);
                orderDetail.setProduct(product);
                orderDetail.setOrder(order);

                // Save to DB
                orderDetail = orderDetailRepository.save(orderDetail);

                return orderDetail;
        }

        @Override
        public OrderDetail getOrderDetailById(Long id) throws Exception {
                return orderDetailRepository.findById(id)
                                .orElseThrow(() -> new DataNotFoundException("Cannot find OrderDetail with ID: " + id));
        }

        @Override
        public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
                // Check if order detail exists
                OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                                .orElseThrow(() -> new DataNotFoundException(
                                                "Cannot find order detail with ID: " + id));

                // Check if order exists
                Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                                .orElseThrow(() -> new DataNotFoundException("Cannot find order with ID: " + id));

                // Check if product exists
                Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                                .orElseThrow(() -> new DataNotFoundException(
                                                "Cannot find product with ID: " + orderDetailDTO.getProductId()));

                // Convert from DTO to model
                existingOrderDetail = modelMapper.map(orderDetailDTO, OrderDetail.class);
                existingOrderDetail.setOrder(existingOrder);
                existingOrderDetail.setProduct(existingProduct);
                existingOrderDetail.setId(id);

                // Save to DB
                existingOrderDetail = orderDetailRepository.save(existingOrderDetail);

                return existingOrderDetail;
        }

        @Override
        public void deleteById(Long id) {
                orderDetailRepository.deleteById(id);
        }

        @Override
        public List<OrderDetail> findByOrderId(Long orderId) {
                return orderDetailRepository.findByOrderId(orderId);
        }
}
