package com.comorosrising.controller;

import com.comorosrising.dto.UserDTO;
import com.comorosrising.mapper.UserMapper;
import com.comorosrising.repository.UserRepository;
import com.comorosrising.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO){
        boolean userUpdated = userService.updateUser(userId, userMapper.fromDTO(userDTO));
        if(userUpdated){
            return ResponseEntity.ok("User updated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId){
        boolean deletedUser = userService.deleteUser(userId);
        if(deletedUser){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
