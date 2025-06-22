package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.Room;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("Room")
public class RoomController {


    @Autowired
    RoomService roomService;
    @Autowired
    JwtUtil jwtUtil;


    @GetMapping("{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return roomService.findById(role, id, user)
                    .map(room -> new ResponseEntity<>(room, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable long id, @RequestBody Room room) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return roomService.findById(role, id, user)
                    .map(existingRoom -> {
                        room.setId(id);
                        Room savedRoom = roomService.update(role, room);
                        return new ResponseEntity<>(savedRoom, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Room> cancelledRoom(@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return roomService.findById(role, id, user)
                    .map(room -> {
                        if (room.isCancelled()) {
                            return new ResponseEntity<Room>(HttpStatus.BAD_REQUEST);
                        }
                        room.setCancelled(true);
                        Room savedRoom = roomService.Cancel(role, room);
                        return new ResponseEntity<>(savedRoom, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
