package com.example.Jbook.services;

import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;

import java.util.Optional;

public interface AdminService {

    User add(Role role, User user);
    Optional<User> findById(Role role, long id, User user);
    User update(Role role, User user);
    User delete(Role role, User user);

}
