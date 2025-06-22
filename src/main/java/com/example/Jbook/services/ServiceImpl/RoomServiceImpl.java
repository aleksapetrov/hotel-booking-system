package com.example.Jbook.services.ServiceImpl;

import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.Room;
import com.example.Jbook.entities.User;
import com.example.Jbook.repositories.RoomRepository;
import com.example.Jbook.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;


    @Override
    public Optional<Room> findById(Role role, long id, User user) {
        if (role == Role.AGENT || role == Role.ADMIN) {
            return roomRepository.findById(id);
        } else if (role == Role.HOTEL) {
            List<Room> rooms = roomRepository.findRoomByHotel(user);
            return rooms.stream()
                    .filter(room -> room.getId() == id)
                    .findFirst();
        } else if (role == Role.CLIENT) {
            List<Room> rooms = roomRepository.findRoomByClient(user);
            return rooms.stream()
                    .filter(room -> room.getId() == id)
                    .findFirst();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid booking number.");
        }
    }

    @Override
    public Room update(Role role, Room room) {
        if ((role == Role.ADMIN || role == Role.AGENT || role == Role.HOTEL) && !room.isCancelled()) {
            return roomRepository.save(room);
        } else if (role == Role.CLIENT && !room.isCancelled()) {
            if (room.getDeadline().isBefore(LocalDate.now())) {
                return roomRepository.save(room);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking is out deadline.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to modify a booking.");
        }
    }

    @Override
    public Room Cancel(Role role, Room room) {
        if ((role == Role.ADMIN || role == Role.AGENT || role == Role.HOTEL) && !room.isCancelled()) {
            room.setCancelled(true);
            return roomRepository.save(room);
        } else if (role == Role.CLIENT && !room.isCancelled()) {
            if (room.getDeadline().isBefore(LocalDate.now())) {
                room.setCancelled(true);
                return roomRepository.save(room);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking is out deadline.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to cancel a booking.");
        }
    }

}
