package com.example.Jbook.services.ServiceImpl;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.repositories.UserRepository;
import com.example.Jbook.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;


    @Override
    public User add(Role role, User user) {

        if (role == Role.ADMIN) {
            return userRepository.save(user);
        } else if (role == Role.AGENT || role == Role.HOTEL || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create a client.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to create a client.");
    }

    @Override
    public Optional<User> findById(Role role, long id, User user) {
        if (role == Role.ADMIN || role == Role.AGENT) {
            return userRepository.findById(id);
        } else if (role == Role.HOTEL) {
            List<User> clients = userRepository.findClientByHotel(user);
            return clients.stream()
                    .filter(client -> client.getId() == id)
                    .findFirst();
        } else if (role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot find other clients.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to find a client.");
    }

    @Override
    public User update(Role role, User user) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (role == Role.ADMIN || role == Role.AGENT) {
            return userRepository.save(user);
        } else if (role == Role.HOTEL) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify a client.");
        } else if (role == Role.CLIENT) {
            if (user.getId() == currentUser.getId()) {
                return userRepository.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify a client.");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to modify a client.");
    }

    @Override
    public User delete(Role role, User user) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (role == Role.ADMIN && !user.isDeleted()) {
            user.setDeleted(true);
        } else if (role == Role.AGENT || role == Role.HOTEL) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete the client.");
        } else if (role == Role.CLIENT) {
            if (user.getId() == currentUser.getId()) {
                user.setDeleted(true);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete other clients.");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to delete a client.");
    }
}
