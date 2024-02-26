package com.example.shopapp.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shopapp.components.JwtTokenUtil;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.PermissionDenyException;
import com.example.shopapp.models.Role;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.services.interfaces.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserDTO registerUser(UserDTO userDTO) throws Exception {
        // Check if phone number exist
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        // Convert from DTO to object
        User user = modelMapper.map(userDTO, User.class);
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Not found role with ID: " + userDTO.getRoleId()));

        // Check if role is ADMIN
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register an admin account");
        }

        // Check if not login by facebook or google
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            user.setRole(role);
        }

        // Save to DB
        user = userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public String loginUser(UserLoginDTO userLoginDTO) throws Exception {
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String password = userLoginDTO.getPassword();

        // Check phone number
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number or password");
        }

        // Check password
        User existingUser = user.get();

        if (existingUser.getFacebookAccountId() == 0 &&
                existingUser.getGoogleAccountId() == 0) {
            boolean isPasswordMatched = passwordEncoder.matches(password,
                    existingUser.getPassword());

            if (!isPasswordMatched) {
                throw new BadCredentialsException("Invalid phone number or password");
            }
        }

        // Authenticate with Java Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,
                existingUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);

        // Generate token
        String token = jwtTokenUtil.genrateToken(existingUser);

        return token;
    }

}
