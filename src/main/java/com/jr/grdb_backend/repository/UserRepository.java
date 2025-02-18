package com.jr.grdb_backend.repository;

import com.jr.grdb_backend.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser,Long> {
    Optional<CustomUser> findByEmail(String email);
}
