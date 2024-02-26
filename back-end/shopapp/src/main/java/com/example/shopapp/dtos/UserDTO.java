package com.example.shopapp.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

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
public class UserDTO {
    private Long id;

    @NotBlank(message = "Fullname is required")
    @Size(max = 100, message = "Fullname must be less than or equal to 100 characters")
    private String fullname;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must be less than or equal to 20 characters")
    private String phoneNumber;

    @Size(max = 200, message = "Address must be less than or equal to 200 characters")
    private String address;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "Retype password is required")
    @Size(min = 6, max = 100, message = "Retype password must be between 6 and 100 characters")
    private String retypePassword;

    private boolean isActive;

    private LocalDate dateOfBirth;

    private int facebookAccountId;

    private int googleAccountId;

    @NotNull(message = "Role ID is required")
    private Long roleId;
}
