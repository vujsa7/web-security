package com.security.user.controller;

import com.security.user.dto.UserDto;
import com.security.user.model.User;
import com.security.user.service.RoleService;
import com.security.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto){
        if(userService.findByEmail(userDto.getEmail()) != null){
            return new ResponseEntity<>(userDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), roleService.findByName("ROLE_USER"));
        userService.save(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
