package com.digiteo.neovoteIV.model.jpa.repository;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT u FROM userEntity u WHERE u.username = ?1")
    Optional<UserEntity> findUserByUsername(String username);

    @Query(value = "SELECT u FROM userEntity u WHERE u.email = ?1")
    Optional<UserEntity> findUserByEmail(String email);

    @Query(value = "SELECT u FROM userEntity u WHERE u.email = :u OR u.username = :u")
    Optional<UserEntity> findUserByUsernameOrEmail(@Param("u") String username);
}
