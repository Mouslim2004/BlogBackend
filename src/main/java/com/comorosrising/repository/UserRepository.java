package com.comorosrising.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.comorosrising.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailAndIdNot(String email, Long userId);

    Optional<User> findByEmail(String email);
}
