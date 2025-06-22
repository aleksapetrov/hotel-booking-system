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
public class AdminServiceImpl implements ClientService {

    @Autowired
    UserAuthRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;


    @Override
    public User add(Role role, User user) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create an admin.");
    }

    @Override
    public Optional<User> findById(Role role, long id, User user) {
        if (role == Role.ADMIN) {
            if (user.getId() == id) {
                return userRepository.findById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot find other admin.");
            }
        } else if (role == Role.AGENT || role == Role.HOTEL || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot find an admin.");
        }
        return userRepository.findById(id);
    }

    @Override
    public User update(Role role, User user) {
        User currentUser = jwtUtil.getCurrentUser().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized."));

        if (role == Role.ADMIN) {
            if (user.getId() == currentUser.getId()) {
                return userRepository.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify other admin.");
            }
        } else if (role == Role.AGENT || role == Role.HOTEL || role == Role.CLIENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify an admin.");
        }
        return userRepository.save(user);
    }

    @Override
    public User delete(Role role, User user) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete an admin.");
    }
}
