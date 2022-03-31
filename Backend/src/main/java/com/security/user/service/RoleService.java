package com.security.user.service;

import com.security.user.model.Role;

public interface RoleService {
    Role findByName(String name);
}
