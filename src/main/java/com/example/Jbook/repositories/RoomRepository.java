package com.example.Jbook.repositories;

import com.example.Jbook.entities.User;
import com.example.Jbook.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomByClient(User client);
    List<Room> findRoomByHotel(User hotel);
}
