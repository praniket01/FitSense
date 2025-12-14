package com.fitness.usermicroservice.DTO;
import com.fitness.usermicroservice.models.Role.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private String password;
    private String firstname;
    private String lastName;
    private String email;
    private  String id;
    private String username;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Role role;
}
