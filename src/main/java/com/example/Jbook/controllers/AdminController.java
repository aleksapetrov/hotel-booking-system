package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("Admin")
public class AdminController {


    @Autowired
    AdminService adminService;
    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("{id}")
    public ResponseEntity<User> postAdmin (@RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                User addUser = adminService.add(role, user);
                return new ResponseEntity<>(addUser, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findAdminById (@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return adminService.findById(role, id, user)
                    .map(admin -> new ResponseEntity<>(admin, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateAdmin (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return adminService.findById(role, id, user)
                    .map(existingClient -> {
                        user.setId(id);
                        User savedAdmin = adminService.update(role, user);
                        return new ResponseEntity<>(savedAdmin, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteAdmin (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return adminService.findById(role, id, user)
                    .map(admin -> {
                        if (admin.isDeleted()) {
                            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
                        }
                        user.setDeleted(true);
                        User savedUser = adminService.delete(role, user);
                        return new ResponseEntity<>(savedUser, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
