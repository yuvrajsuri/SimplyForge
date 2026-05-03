package com.yuvraj.SimplyForge.Service;

import com.yuvraj.SimplyForge.dto.Auth.UserProfileResponse;

public interface UserService {

    UserProfileResponse getProfile(Long userId);

}
