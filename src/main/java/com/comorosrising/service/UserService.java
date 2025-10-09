package com.comorosrising.service;

import com.comorosrising.entity.User;
import com.comorosrising.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void createUser(User user){
        if(user == null){
            throw new IllegalArgumentException("User details must be provided");
        }
        logger.info("Creating new user: {}", user.getEmail());
        if(user.getName() == null || user.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Name is required");
        }
        if(user.getEmail() == null || user.getEmail().trim().isEmpty()){
            throw new IllegalArgumentException("Email is required");
        }
        if(user.getPassword() == null || user.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("Password is required");
        }
        if(user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if(!isValidEmail(user.getEmail())){
            throw new IllegalArgumentException("Invalid email format");
        }
        if(user.getDateOfBirth().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Date of birth must be in the past");
        }
        if(user.getDateOfBirth() == null){
            throw new IllegalArgumentException("Date of birth must be provided");
        }
        if(user.getJoinedAt() == null){
            user.setJoinedAt(LocalDateTime.now());
        }
        if(user.getUpdatedAt() == null){
            user.setUpdatedAt(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean updateUser(Long userId, User user){
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()){
            User userToSave = userOptional.get();
            if(user.getName() != null && !user.getName().trim().isEmpty()) {
                userToSave.setName(user.getName());
            }
            if(user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                // Check if email already exists for other users
                if(userRepository.existsByEmailAndIdNot(user.getEmail(), userId)) {
                    throw new IllegalArgumentException("Email already exists");
                }
                userToSave.setEmail(user.getEmail());
            }
            userToSave.setBio(user.getBio());
            userToSave.setDateOfBirth(user.getDateOfBirth());
            userToSave.setUpdatedAt(LocalDateTime.now());
            userRepository.save(userToSave);
            return true;
        }

        return false;
    }

    public boolean deleteUser(Long userId){
        if(!userRepository.existsById(userId)) {
            return false;
        }
        try{
            userRepository.deleteById(userId);
            return true;
        }catch(Exception e){
            logger.error("Error deleting user with id: " + userId, e);
            return false;
        }
    }
}
