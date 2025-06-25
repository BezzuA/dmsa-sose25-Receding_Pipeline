package com.parkandcharge.userservice.dto;

import com.parkandcharge.userservice.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role; // OWNER or CAR_USER
}
