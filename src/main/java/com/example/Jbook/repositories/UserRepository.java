package com.example.Jbook.repositories;

import com.example.Jbook.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findClientByHotel(User client);
    User promote (User user);
    User demote (User user);

}
