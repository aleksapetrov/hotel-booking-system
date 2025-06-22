package com.example.Jbook.services;

import com.example.Jbook.entities.User;

import java.util.Optional;

public interface UserAuthService {

    Optional<User> findByUsername(String username);
    User register(User user);
    String login(String username, String password);

}
