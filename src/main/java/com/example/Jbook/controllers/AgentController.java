package com.example.Jbook.controllers;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("Agent")
public class AgentController {


    @Autowired
    AgentService agentService;
    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("{id}")
    public ResponseEntity<User> postAgent (@RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            try {
                User addUser = agentService.add(role, user);
                return new ResponseEntity<>(addUser, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findAgentById (@PathVariable long id) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role role = jwtUtil.getCurrentUserRole();

            return agentService.findById(role, id, user)
                    .map(agent -> new ResponseEntity<>(agent, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateAgent (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return agentService.findById(role, id, user)
                    .map(existingClient -> {
                        user.setId(id);
                        User savedAgent = agentService.update(role, user);
                        return new ResponseEntity<>(savedAgent, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteAgent (@PathVariable long id, @RequestBody User user) {
        Optional<User> userOptional = jwtUtil.getCurrentUser();
        if (userOptional.isPresent()) {
            Role role = jwtUtil.getCurrentUserRole();

            return agentService.findById(role, id, user)
                    .map(agent -> {
                        if (agent.isDeleted()) {
                            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
                        }
                        user.setDeleted(true);
                        User savedUser = agentService.delete(role, user);
                        return new ResponseEntity<>(savedUser, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
