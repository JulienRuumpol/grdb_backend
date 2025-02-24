package com.jr.grdb_backend.repository;

import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser,Long> {
    Optional<CustomUser> findByEmail(String email);

    @Query("SELECT u.games FROM CustomUser u WHERE u.id = :userId")
    List<Game> findGamesByUserId(@Param("userId") Long userId);

}
