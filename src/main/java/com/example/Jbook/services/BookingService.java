package com.example.Jbook.services;

import com.example.Jbook.entities.Booking;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking add(Role role, Booking booking);
    Optional<Booking> findById(Role role, long id, User user);
    Booking update(Role role, Booking booking);
    Booking Cancel(Role role, Booking booking);

}
