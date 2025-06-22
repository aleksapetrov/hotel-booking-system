package com.example.Jbook.Security;

import com.example.Jbook.entities.User;
import com.example.Jbook.entities.Role;
import com.example.Jbook.services.UserAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtUtil {

    private final UserAuthService userAuthService;

    public JwtUtil(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userAuthService.findByUsername(userDetails.getUsername());
        }
        return Optional.empty();
    }

    public Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return Role.valueOf(authentication.getAuthorities().iterator().next().getAuthority());
        }
        throw new RuntimeException("Role not found for current user");
    }
}
