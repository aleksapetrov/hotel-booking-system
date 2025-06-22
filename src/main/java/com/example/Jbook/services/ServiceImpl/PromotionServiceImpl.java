package com.example.Jbook.services.ServiceImpl;

import com.example.Jbook.Security.JwtUtil;
import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;
import com.example.Jbook.repositories.UserRepository;
import com.example.Jbook.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PromotionServiceImpl implements PromotionService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User promote(Role role, long id) {
        if (role == Role.ADMIN) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found."));

            if (user.getRole() == Role.AGENT) {
                user.setRole(Role.ADMIN);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Agent can be promoted only to admin.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin can promote other agents.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to promote.");
    }

    @Override
    public User demote(Role role, long id) {
        if (role == Role.ADMIN) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found."));

            if (user.getRole() == Role.ADMIN) {
                user.setRole(Role.AGENT);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin can be demoted only to agent.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin can demoted other admins.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized to demote.");
    }

}
