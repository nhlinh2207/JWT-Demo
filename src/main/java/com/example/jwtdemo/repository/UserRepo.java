package com.example.jwtdemo.repository;

import com.example.jwtdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByEmail(String email);

}
