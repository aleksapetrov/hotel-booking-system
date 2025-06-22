package com.example.Jbook.services.ServiceImpl;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Booking;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.repositories.BookingRepository;
import com.example.Jbook.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    JwtUtil jwtUtil;


    @Override
    public Booking add(Role role, Booking booking) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (booking.getCreditCard() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking must contain credit card.");
        }

        if (booking.getRooms() == null || booking.getRooms().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking must have at least one room.");
        }

        if (role == Role.AGENT || role == Role.ADMIN) {
            booking.setCreatedAt(LocalDate.now());
            return bookingRepository.save(booking);
        } else if (role == Role.HOTEL) {
            if (booking.getHotel().equals(currentUser)) {
                booking.setCreatedAt(LocalDate.now());
                return bookingRepository.save(booking);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot add booking for another hotel.");
            }
        } else if (role == Role.CLIENT) {
            if (booking.getClient().equals(currentUser)) {
                booking.setCreatedAt(LocalDate.now());
                return bookingRepository.save(booking);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot add booking for another client.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to create the booking.");
        }
    }

    @Override
    public Optional<Booking> findById(Role role, long id, User user) {
        if (role == Role.AGENT || role == Role.ADMIN) {
            return bookingRepository.findById(id);
        } else if (role == Role.HOTEL) {
            List<Booking> bookings = bookingRepository.findBookingByHotel(user);
            return bookings.stream()
                    .filter(booking -> booking.getId() == id)
                    .findFirst();
        } else if (role == Role.CLIENT) {
            List<Booking> bookings = bookingRepository.findBookingByClient(user);
            return bookings.stream()
                    .filter(booking -> booking.getId() == id)
                    .findFirst();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid booking number.");
        }
    }

    @Override
    public Booking update(Role role, Booking booking) {

        if ((booking.getCreditCard() == null)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking must contain credit card.");
        }

        if (booking.getRooms() == null || booking.getRooms().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking must have at least one room.");
        }

        if ((role == Role.AGENT || role == Role.ADMIN || role == Role.HOTEL) && !booking.isCancelled()) {
            booking.setModifiedAt(LocalDate.now());
            return bookingRepository.save(booking);
        } else if (role == Role.CLIENT && !booking.isCancelled()) {
            boolean allRoomsOutOfDeadline = booking.getRooms().stream()
                    .allMatch(room -> room.getDeadline().isAfter(LocalDate.now()));

            if (allRoomsOutOfDeadline) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking is out deadline.");
            } else {
                booking.setModifiedAt(LocalDate.now());
                return bookingRepository.save(booking);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to modify a booking.");
        }
    }

    @Override
    public Booking Cancel(Role role, Booking booking) {
        if ((role == Role.ADMIN || role == Role.AGENT || role == Role.HOTEL) && !booking.isCancelled()) {
            booking.setCancelledAt(LocalDate.now());
            return bookingRepository.save(booking);
        } else if (role == Role.CLIENT && !booking.isCancelled()) {
            boolean allRoomsOutOfDeadline = booking.getRooms().stream()
                    .allMatch(room -> room.getDeadline().isAfter(LocalDate.now()));

            if (allRoomsOutOfDeadline) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking is out deadline.");
            } else {
                booking.setModifiedAt(LocalDate.now());
                return bookingRepository.save(booking);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to cancel a booking.");
        }
    }

}
