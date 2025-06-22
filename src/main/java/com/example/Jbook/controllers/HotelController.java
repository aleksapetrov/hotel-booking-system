package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("Hotel")
public class HotelController {


    @Autowired
    HotelService hotelService;
    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("{id}")
    public ResponseEntity<User> postHotel (@RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                User addUser = hotelService.add(role, user);
                return new ResponseEntity<>(addUser, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findHotelById (@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return hotelService.findById(role, id, user)
                    .map(hotel -> new ResponseEntity<>(hotel, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateHotel (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return hotelService.findById(role, id, user)
                    .map(existingClient -> {
                        user.setId(id);
                        User savedHotel = hotelService.update(role, user);
                        return new ResponseEntity<>(savedHotel, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteHotel (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return hotelService.findById(role, id, user)
                    .map(hotel -> {
                        if (hotel.isDeleted()) {
                            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
                        }
                        user.setDeleted(true);
                        User savedUser = hotelService.delete(role, user);
                        return new ResponseEntity<>(savedUser, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
