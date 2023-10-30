package com.example.BloggingpApi.service;

import com.example.BloggingpApi.model.AuthenticationToken;
import com.example.BloggingpApi.model.User;
import com.example.BloggingpApi.repository.IAuthenticationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    IAuthenticationRepo authenticationRepo;

    public void saveAuthToken(AuthenticationToken authToken) {
        authenticationRepo.save(authToken);
    }

    public boolean authenticate(String email, String token) {
        AuthenticationToken authenticationToken = authenticationRepo.findFirstByTokenValue(token);

        if(authenticationToken == null){
            return false;
        }

        String tokenConnectedEmail = authenticationToken.getUser().getUserEmail();
        return tokenConnectedEmail.equals(email);
    }

    public void removeToken(AuthenticationToken authenticationToken) {
        authenticationRepo.delete(authenticationToken);
    }

    public AuthenticationToken findFirstByUser(User user) {
        return authenticationRepo.findFirstByUser(user);
    }
}