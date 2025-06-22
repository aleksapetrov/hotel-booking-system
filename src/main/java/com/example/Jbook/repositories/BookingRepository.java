package com.example.Jbook.repositories;

import com.example.Jbook.entities.Booking;
import com.example.Jbook.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByClient(User client);
    List<Booking> findBookingByHotel(User hotel);
}
