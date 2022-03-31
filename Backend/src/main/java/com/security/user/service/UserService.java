package com.security.user.service;

import com.security.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void save(User user);
    User findByEmail(String email);
}
