package com.example.Jbook.Security;

import com.example.Jbook.entities.Role;
import lombok.Data;

@Data
public class RegisterDTO {

    private String email;
    private String password;
    private Role role;


}
