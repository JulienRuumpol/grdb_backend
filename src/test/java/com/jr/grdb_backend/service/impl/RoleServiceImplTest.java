package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.model.Role;
import com.jr.grdb_backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Important!
class RoleServiceImplTest {
    @InjectMocks
    private RoleServiceImpl roleServiceImpl;
    @Mock
    private RoleRepository roleRepository;
    private Role role;


    @BeforeEach
    void setUp() {
        this.role = new Role();
        role.setId(1l);
    }

    /**
     * Unit test {@link RoleServiceImpl#getAll()}
     * Scenario: Successfully retrieve all roles
     */
    @Test
    void getAllRoles() {
        when(this.roleRepository.findAll()).thenReturn(Collections.singletonList(this.role));
        this.setRoleName("Admin");

        List<Role> roles = this.roleServiceImpl.getAll();
        verify(this.roleRepository, times(1)).findAll();

        assertEquals(1, roles.size());

    }

    private void setRoleName(String name) {
        this.role.setName(name);
    }
}