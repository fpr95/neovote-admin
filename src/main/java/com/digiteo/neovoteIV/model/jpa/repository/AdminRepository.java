package com.digiteo.neovoteIV.model.jpa.repository;

import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    @Query(value = "SELECT u FROM adminEntity u WHERE u.username = ?1")
    Optional<AdminEntity> findUserByUsername(String username);

    @Query(value = "SELECT u FROM adminEntity u WHERE u.email = ?1")
    Optional<AdminEntity> findUserByEmail(String email);

    @Query(value = "SELECT u FROM adminEntity u WHERE u.email = :u OR u.username = :u")
    Optional<AdminEntity> findUserByUsernameOrEmail(@Param("u") String username);
}