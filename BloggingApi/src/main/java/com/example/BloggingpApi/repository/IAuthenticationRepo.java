package com.example.BloggingpApi.repository;

import com.example.BloggingpApi.model.AuthenticationToken;
import com.example.BloggingpApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuthenticationRepo extends JpaRepository<AuthenticationToken, Long> {
    AuthenticationToken findFirstByTokenValue(String token);


    AuthenticationToken findFirstByUser(User user);
}