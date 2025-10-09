package com.comorosrising.mapper;

import com.comorosrising.dto.UserDTO;
import com.comorosrising.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromDTO(UserDTO userDTO){
        return new User(
                userDTO.id(),
                userDTO.name(),
                userDTO.email(),
                userDTO.password(),
                userDTO.bio(),
                userDTO.dateOfBirth(),
                null,
                null,
                null,
                null

        );
    }

    public UserDTO toDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getBio(),
                user.getDateOfBirth()
        );
    }
}
