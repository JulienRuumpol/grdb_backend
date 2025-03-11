package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.model.Role;
import com.jr.grdb_backend.repository.RoleRepository;
import com.jr.grdb_backend.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl  implements RoleService {


    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return this.roleRepository.findAll();
    }
}
