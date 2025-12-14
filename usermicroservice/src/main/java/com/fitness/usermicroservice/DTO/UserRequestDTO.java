package com.fitness.usermicroservice.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6,max = 15,message = "Password should be between 6 to 15 characters")

    private String password;
    private String firstname;
    private String lastName;
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid Email format")
    private String email;
}
