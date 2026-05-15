package com.yuvraj.SimplyForge.Service.Impl;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.UserService;
import com.yuvraj.SimplyForge.dto.Auth.UserProfileResponse;

@Service
public class UserServiceImpl implements UserService{

   UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

}
