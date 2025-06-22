package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("Client")
public class ClientController {


    @Autowired
    ClientService clientService;
    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("{id}")
    public ResponseEntity<User> postClient (@RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                User addUser = clientService.add(role, user);
                return new ResponseEntity<>(addUser, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findClientById (@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return clientService.findById(role, id, user)
                    .map(client -> new ResponseEntity<>(client, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateClient (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return clientService.findById(role, id, user)
                    .map(existingClient -> {
                        user.setId(id);
                        User savedClient = clientService.update(role, user);
                        return new ResponseEntity<>(savedClient, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteClient (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return clientService.findById(role, id, user)
                    .map(client -> {
                        if (client.isDeleted()) {
                            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
                        }
                        user.setDeleted(true);
                        User savedUser = clientService.delete(role, user);
                        return new ResponseEntity<>(savedUser, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
