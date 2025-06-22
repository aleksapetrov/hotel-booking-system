package com.example.Jbook.services;

import com.example.Jbook.entities.Role;
import com.example.Jbook.entities.User;

public interface PromotionService {

    User promote (Role role, long id);
    User demote (Role role, long id);

}
