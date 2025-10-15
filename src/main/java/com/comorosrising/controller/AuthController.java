package com.comorosrising.controller;

import com.comorosrising.dto.UserDTO;
import com.comorosrising.dto.UserLoginDTO;
import com.comorosrising.mapper.UserMapper;
import com.comorosrising.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO){
        userService.createUser(userMapper.fromDTO(userDTO));
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO){
        String token = userService.login(userLoginDTO);
        return new ResponseEntity<>(
                Map.of("token", token, "message", "Logged in successfully"),
                HttpStatus.OK);
    }
}
