package com.fitness.usermicroservice.repository;

import com.fitness.usermicroservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<User,String> {

}
