package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Booking;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("Booking")
public class BookingController {


    @Autowired
    BookingService bookingService;
    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("{id}")
    public ResponseEntity<Booking> postBooking(@RequestBody Booking booking) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                Booking addBooking = bookingService.add(role, booking);
                return new ResponseEntity<>(addBooking, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return bookingService.findById(role, id, user)
                    .map(booking -> new ResponseEntity<>(booking, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable long id, @RequestBody Booking booking) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return bookingService.findById(role, id, user)
                    .map(existingBooking -> {
                        booking.setId(id);
                        Booking savedBooking = bookingService.update(role, booking);
                        return new ResponseEntity<>(savedBooking, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable long id, @RequestBody Booking booking) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return bookingService.findById(role, id, user)
                    .map(existingBooking -> {
                        if (existingBooking.isCancelled()) {
                            return new ResponseEntity<Booking>(HttpStatus.BAD_REQUEST);
                        }
                        booking.setCancelled(true);
                        Booking savedBooking = bookingService.Cancel(role, booking);
                        return new ResponseEntity<>(savedBooking, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
