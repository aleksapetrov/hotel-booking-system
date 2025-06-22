package com.example.Jbook.services;

import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.Room;
import com.example.Jbook.entities.User;

import java.util.Optional;

public interface RoomService {

    Optional<Room> findById(Role role, long id, User user);
    Room update(Role role, Room room);
    Room Cancel(Role role, Room room);

}
