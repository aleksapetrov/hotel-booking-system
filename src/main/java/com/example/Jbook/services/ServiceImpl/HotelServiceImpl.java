package com.example.Jbook.services.ServiceImpl;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.repositories.UserRepository;
import com.example.Jbook.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;


    @Override
    public User add(Role role, User user) {
        if (role == Role.ADMIN) {
            return userRepository.save(user);
        } if (role == Role.AGENT || role == Role.HOTEL || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create a hotel.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to create a hotel.");
    }

    @Override
    public Optional<User> findById(Role role, long id, User user) {
        if (role == Role.ADMIN || role == Role.AGENT) {
            return userRepository.findById(id);
        } else if (role == Role.HOTEL) {
            if (user.getId() == id) {
                return userRepository.findById(id);
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot find other hotels.");
        } else if (role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot find hotels.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to find a hotel.");
    }

    @Override
    public User update(Role role, User user) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (role == Role.ADMIN) {
            return userRepository.save(user);
        } else if (role == Role.AGENT || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify hotels.");
        } else if (role == Role.HOTEL) {
            if (user.getId() == currentUser.getId()) {
                return userRepository.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify other hotels.");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to modify a hotel.");
    }

    @Override
    public User delete(Role role, User user) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (role == Role.ADMIN && !user.isDeleted()) {
            user.setDeleted(true);
        } else if (role == Role.AGENT || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete hotels.");
        } else if (role == Role.HOTEL && !user.isDeleted()) {
            if (user.getId() == currentUser.getId()) {
                user.setDeleted(true);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete other hotels.");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to delete a hotel.");
    }
}
