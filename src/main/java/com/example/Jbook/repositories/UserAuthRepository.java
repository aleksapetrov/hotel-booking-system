package com.example.Jbook.repositories;

import com.example.Jbook.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
