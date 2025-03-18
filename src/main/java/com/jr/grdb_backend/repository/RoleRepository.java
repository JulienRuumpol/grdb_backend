package com.jr.grdb_backend.repository;

import com.jr.grdb_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
