package com.example.Jbook.services.ServiceImpl;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Booking;
import com.example.Jbook.entities.CreditCard;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.repositories.CreditCardRepository;
import com.example.Jbook.services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    JwtUtil jwtUtil;


    @Override
    public CreditCard add(Role role, CreditCard creditCard) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (creditCard == null || creditCard.getCcNumber() == null || creditCard.getType() == null || creditCard.getValidDate() == null || creditCard.getCVC() == null || creditCard.getCcHolder() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credit card must not have empty field.");
        }

        if (role == Role.ADMIN || role == Role.AGENT) {
            return creditCardRepository.save(creditCard);
        } else if (role == Role.HOTEL) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to add credit card.");
        } else if (role == Role.CLIENT) {
            if (creditCard.getClient().equals(currentUser)) {
                return creditCardRepository.save(creditCard);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to add a credit card.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to add a credit card.");
        }
    }

    @Override
    public CreditCard update(Role role, CreditCard creditCard) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (creditCard == null || creditCard.getCcNumber() == null || creditCard.getType() == null || creditCard.getValidDate() == null || creditCard.getCVC() == null || creditCard.getCcHolder() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credit card must not have empty field.");
        }

        boolean bookingOutOfDeadline = creditCard.getBooking().getRooms().stream()
                .anyMatch(room -> room.getDeadline().isAfter(LocalDate.now()));

        if (role == Role.ADMIN || role == Role.AGENT) {
            return creditCardRepository.save(creditCard);
        } else if (role == Role.HOTEL) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to modify a credit card.");
        } else if (role == Role.CLIENT) {
            if (creditCard.getClient().equals(currentUser) && !bookingOutOfDeadline) {
                return creditCardRepository.save(creditCard);
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to modify a credit card.");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to modify a credit card.");
        }
    }

    @Override
    public void Cancel(Booking booking, CreditCard creditCard) {
        if (booking.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cancelled booking cannot be charged.");
        }
    }

}
