package com.sarz.electronic.store.Service.impl;

import com.sarz.electronic.store.entities.User;
import com.sarz.electronic.store.exceptions.ResourceNotFoundException;
import com.sarz.electronic.store.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User Email Not Found!!"));
        return user;

    }
}
