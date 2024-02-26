package com.example.shopapp.dtos;

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
public class UserLoginDTO {
    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must be less than or equal to 20 characters")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
