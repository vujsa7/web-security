package com.security.user.service;

import com.security.certificate.model.Certificate;
import com.security.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void save(User user);
    void save(User user, Certificate certificate);
    User findByEmail(String email);
    User generateDefaultUser(String email);
}
