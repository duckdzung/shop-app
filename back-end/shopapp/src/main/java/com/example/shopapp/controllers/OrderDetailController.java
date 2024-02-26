package com.example.shopapp.controllers;

import com.example.shopapp.dtos.*;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errors);
            }

            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(newOrderDetail);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating order detail");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@PathVariable Long id) {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
            return ResponseEntity.ok().body(orderDetail);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting order detail by ID");
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
        try {
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting order detail by order ID");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @PathVariable Long id,
            @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errors);
            }

            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating order detail");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        try {
            orderDetailService.deleteById(id);
            return ResponseEntity.ok().body("Delete order detail with id : " + id + " successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting order detail");
        }
    }
}
