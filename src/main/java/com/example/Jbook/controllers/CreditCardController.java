package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Booking;
import com.example.Jbook.entities.CreditCard;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("CreditCard")
public class CreditCardController {


    @Autowired
    CreditCardService creditCardService;
    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("{id}")
    public ResponseEntity<CreditCard> postCreditCard (@RequestBody CreditCard creditCard) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                CreditCard addCreditCard = creditCardService.add(role, creditCard);
                return new ResponseEntity<>(addCreditCard, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CreditCard> putCreditCard (@PathVariable long id, @RequestBody CreditCard creditCard) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            creditCard.setId(id);

            try {
                CreditCard savedCreditCard = creditCardService.update(role, creditCard);
                return new ResponseEntity<>(savedCreditCard, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CreditCard> cancelCreditCard (@PathVariable long id, @RequestBody Booking booking) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {

            CreditCard creditCard = new CreditCard();
            creditCard.setId(id);

            try {
                creditCardService.Cancel(booking, creditCard);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
