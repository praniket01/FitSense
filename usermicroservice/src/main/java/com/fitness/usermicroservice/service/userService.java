package com.fitness.usermicroservice.service;

import com.fitness.usermicroservice.DTO.UserRequestDTO;
import com.fitness.usermicroservice.DTO.UserResponse;
import com.fitness.usermicroservice.config.ModelMapper;
import com.fitness.usermicroservice.models.User;
import com.fitness.usermicroservice.repository.userRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class userService {
    public final userRepository userRepository;

    public UserResponse addUser( UserRequestDTO userRequest) {

//        if(userRepository.findByEmail(userRequest.getEmail())){
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
//        }
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setFirstname(userRequest.getEmail());
        user.setLastName(userRequest.getFirstname());

        User savedUser = (User) userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstname(savedUser.getFirstname());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        userResponse.setRole(savedUser.getRole());
        return userResponse;

    }

    public UserResponse getUserById(String id) {
      try{
          User user = userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
          UserResponse userResponse = new UserResponse();
          userResponse.setId(user.getId());
          userResponse.setEmail(user.getEmail());
          userResponse.setFirstname(user.getFirstname());
          userResponse.setLastName(user.getLastName());
          userResponse.setPassword(user.getPassword());
          userResponse.setCreatedAt(user.getCreatedAt());
          userResponse.setUpdatedAt(user.getUpdatedAt());
          return  userResponse;
      }catch (Exception e){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
    }

    public  Boolean validateUser(String  id){
        return userRepository.existsById(id);
    }
}
