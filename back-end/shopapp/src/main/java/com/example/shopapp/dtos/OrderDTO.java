package com.example.shopapp.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String note;

    @NotNull(message = "Total money is required")
    private Double totalMoney;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Shipping method is required")
    private String shippingMethod;

    @NotNull(message = "Shipping date is required")
    private LocalDate shippingDate;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String status;

    private Boolean isActive;

    private String trackingNumber;
}
