package com.comorosrising.mapper;

import com.comorosrising.dto.UserDTO;
import com.comorosrising.dto.UserOutputDTO;
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

    public UserOutputDTO toOutputDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserOutputDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBio(),
                user.getDateOfBirth(),
                user.getJoinedAt()
        );
    }

    public User fromOutputDTO(UserOutputDTO userOutputDTO) {
        if (userOutputDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userOutputDTO.id());
        user.setName(userOutputDTO.name());
        user.setEmail(userOutputDTO.email());
        user.setBio(userOutputDTO.bio());
        user.setDateOfBirth(userOutputDTO.dateOfBirth());
        user.setJoinedAt(userOutputDTO.joinedAt());
        return user;
    }


}
