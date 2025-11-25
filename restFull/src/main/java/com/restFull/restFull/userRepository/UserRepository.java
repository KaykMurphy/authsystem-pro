package com.restFull.restFull.userRepository;

import com.restFull.restFull.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // to search
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // to check if it exists
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


}
