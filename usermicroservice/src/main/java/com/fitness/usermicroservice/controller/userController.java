package com.fitness.usermicroservice.controller;
import com.fitness.usermicroservice.DTO.UserRequestDTO;
import com.fitness.usermicroservice.DTO.UserResponse;
import com.fitness.usermicroservice.service.userService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class userController {
    public final userService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequestDTO userRequest){
        return ResponseEntity.ok(userService.addUser(userRequest));
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String id){
         return ResponseEntity.ok(userService.validateUser(id));
    }
}
