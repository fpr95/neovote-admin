package com.digiteo.neovoteIV.model.jpa.repository;

import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<VoterEntity, Long> {

    @Query(value = "SELECT u FROM voterEntity u WHERE u.username = ?1")
    Optional<VoterEntity> findVoterByUsername(String username);

    @Query(value = "SELECT u FROM voterEntity u WHERE u.email = ?1")
    Optional<VoterEntity> findVoterByEmail(String email);

    @Query(value = "SELECT u FROM voterEntity u WHERE u.email = :u OR u.username = :u")
    Optional<VoterEntity> findVoterByUsernameOrEmail(@Param("u") String username);
}
