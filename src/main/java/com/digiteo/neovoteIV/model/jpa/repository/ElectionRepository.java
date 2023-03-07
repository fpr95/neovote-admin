package com.digiteo.neovoteIV.model.jpa.repository;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {

    @Query(value = "SELECT e FROM election e WHERE e.title = ?1")
    Optional<Election> findElectionByTitle(String title);
}
