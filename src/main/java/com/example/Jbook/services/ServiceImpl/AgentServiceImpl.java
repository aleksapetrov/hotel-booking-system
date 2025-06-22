package com.example.Jbook.services.ServiceImpl;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.repositories.UserAuthRepository;
import com.example.Jbook.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AgentServiceImpl implements ClientService {

    @Autowired
    UserAuthRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;


    @Override
    public User add(Role role, User user) {
        if (role == Role.ADMIN) {
            return userRepository.save(user);
        } else if (role == Role.AGENT || role == Role.HOTEL || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create an agent.");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to create an agent.");
        }
    }

    @Override
    public Optional<User> findById(Role role, long id, User user) {
        if (role == Role.ADMIN) {
            return userRepository.findById(id);
        } else if (role == Role.AGENT) {
            if (user.getId() == id) {
                return userRepository.findById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot find other agents.");
            }
        } else if (role == Role.HOTEL || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot find an agent.");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to find an agent.");
        }
    }

    @Override
    public User update(Role role, User user) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (role == Role.ADMIN) {
            return userRepository.save(user);
        } else if (role == Role.AGENT) {
            if (user.getId() == currentUser.getId()) {
                return userRepository.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update other agents.");
            }
        } else if (role == Role.HOTEL || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify an agent.");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to find an agent.");
        }
    }

    @Override
    public User delete(Role role, User user) {

        if (role == Role.ADMIN && !user.isDeleted()) {
            user.setDeleted(true);
        } else if ((role == Role.AGENT || role == Role.HOTEL || role == Role.CLIENT) && !user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete an agent.");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to other agents.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to delete an agent.");
    }
}
