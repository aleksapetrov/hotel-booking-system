package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("Promotion")
public class PromotionController {


    @Autowired
    PromotionService promotionService;
    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("promote/{id}")
    public ResponseEntity<User> promoteToAdmin (@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                User promoteUser = promotionService.promote(role, id);
                return new ResponseEntity<>(promoteUser, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("demote/{id}")
    public ResponseEntity<User> demoteToAgent (@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                User demoteUser = promotionService.demote(role, id);
                return new ResponseEntity<>(demoteUser, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
