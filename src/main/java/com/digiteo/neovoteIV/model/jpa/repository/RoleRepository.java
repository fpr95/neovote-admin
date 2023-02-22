package com.digiteo.neovoteIV.model.jpa.repository;

import com.digiteo.neovoteIV.model.jpa.data.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
