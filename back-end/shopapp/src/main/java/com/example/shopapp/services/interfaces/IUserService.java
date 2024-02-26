package com.example.shopapp.services.interfaces;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;

public interface IUserService {
    UserDTO registerUser(UserDTO userDTO) throws Exception;

    String loginUser(UserLoginDTO userLoginDTO) throws Exception;
}
