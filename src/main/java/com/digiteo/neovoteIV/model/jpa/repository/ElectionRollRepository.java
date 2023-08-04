package com.digiteo.neovoteIV.model.jpa.repository;

import com.digiteo.neovoteIV.model.jpa.data.ElectionRoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElectionRollRepository extends JpaRepository<ElectionRoll, Long> {

    @Query(value = "SELECT u FROM electionRoll u WHERE u.voterUsername = ?1")
    Optional<ElectionRoll> findRollUsername(String username);
}
