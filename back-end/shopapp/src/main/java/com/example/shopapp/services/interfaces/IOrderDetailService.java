package com.example.shopapp.services.interfaces;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;

    OrderDetail getOrderDetailById(Long id) throws Exception;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws Exception;

    void deleteById(Long id);

    List<OrderDetail> findByOrderId(Long orderId);

}
