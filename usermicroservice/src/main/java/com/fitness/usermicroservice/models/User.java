package com.fitness.usermicroservice.models;

import com.fitness.usermicroservice.models.Role.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    private  String id;
    private String username;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
    private String firstname;
    private String lastName;

    @Column(unique = true)
    private String email;

}
