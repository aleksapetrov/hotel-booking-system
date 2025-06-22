package com.example.Jbook.controllers;

import com.example.Jbook.Security.LoginDTO;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Authentication")
public class AuthenticationController {


    @Autowired
    UserAuthService userAuthService;


    @RequestMapping("register")
    public ResponseEntity<User> registerUser (@RequestBody User user) {
        userAuthService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("login")
    public ResponseEntity<User> loginUser (@RequestBody LoginDTO loginDTO) {
        userAuthService.login(loginDTO.getEmail(), loginDTO.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
