package com.security.user.service.impl;

import com.security.certificate.dao.CertificateRepository;
import com.security.certificate.model.Certificate;
import com.security.user.dao.UserRepository;
import com.security.user.model.User;
import com.security.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CertificateRepository certificateRepository){
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if(user == null){
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", s));
        } else {
            return user;
        }
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void save(User user, Certificate certificate) {
        userRepository.save(user);
        certificateRepository.save(certificate);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
